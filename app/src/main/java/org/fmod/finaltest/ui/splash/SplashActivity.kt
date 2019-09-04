package org.fmod.finaltest.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.fmod.finaltest.R
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.ui.login.LoginActivity
import org.fmod.finaltest.ui.main.MainActivity
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.toast
import java.util.concurrent.TimeUnit

//欢迎界面显示时间
private const val WELCOME_TIME = 500L   //ms

class SplashActivity : BaseActivity(), SplashContract.View {

    private var createTime = 0L

    private lateinit var presenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectPresenter(SplashPresenter(this))
        createTime = System.currentTimeMillis()

        presenter.tryLogin()
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun setListener() {

    }

    override fun initView() {
        splash_icon.layoutParams.run{
            height = width
        }
    }

    override fun onBackPressed() {
        //不允许退出
    }


    /* SplashContract.View Methods */
    override fun injectPresenter(presenter: SplashContract.Presenter) {
        this.presenter = presenter
    }

    @SuppressLint("CheckResult")
    override fun finishLogin(success: Boolean) {
        if(success) {
            val passedTime = System.currentTimeMillis() - createTime
            if(passedTime > WELCOME_TIME) {
                startActivity(MainActivity::class.java, 0, 0)
            } else {
                Observable.timer(WELCOME_TIME - passedTime, TimeUnit.MILLISECONDS)
                    .bindToLifecycle(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        startActivity(MainActivity::class.java, 0,0)
                        finish()
                    }
            }
        } else {
            toast("网络好像有、问题")

            Observable.timer(WELCOME_TIME, TimeUnit.MILLISECONDS)
                .bindToLifecycle(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity(LoginActivity::class.java, 0,0)
                    finish()
                }
        }
    }

    /**
     * 没有登录状态或者设置不自动登录
     * 直接进入登录界面
     */
    @SuppressLint("CheckResult")
    override fun noLogin() {
        val passedTime = System.currentTimeMillis() - createTime
        Observable.timer(WELCOME_TIME - passedTime, TimeUnit.MILLISECONDS)
            .bindToLifecycle(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(LoginActivity::class.java, 0,0)
                finish()
            }
    }

}
