package org.fmod.finaltest.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.base.abstracts.RemoteObserver
import org.fmod.finaltest.bean.remote.BaseRes
import org.fmod.finaltest.bean.remote.Login
import org.fmod.finaltest.bean.remote.State
import org.fmod.finaltest.helper.pref.PreferenceHelper
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.helper.remote.RemoteQQ
import org.fmod.finaltest.util.toplevel.toast

class LoginPresenter(
    private val mView: LoginContract.View
): LoginContract.Presenter {

    private lateinit var email: String
    private lateinit var password: String

    override fun start() {

    }

    override fun prepareLogin(context: Context) {
        RemoteHelper.logoutQQ(context)
    }

    override fun login() {

        mView.showProgress()

        RemoteHelper.login(email, password)
            .bindToLifecycle(mView)
            .doOnNext {
                if (it.state != 200) {
                    toast("邮箱或密码错误")
                    mView.hideProgress()
                }
            }
            .filter {
                it.state == 200
            }
            .subscribe(object : RemoteObserver<BaseRes<Login>>() {
                override fun onNext(t: BaseRes<Login>) {
                    PreferenceHelper.saveMailLogin(email, password)
                    PreferenceHelper.setLoginWay(PreferenceHelper.wayEmail)
                    MyApp.token = t.result.token
                    mView.finishLogin()
                    mView.hideProgress()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    toast("请检查网络")
                    mView.hideProgress()
                }
            })
    }

    /**
     * QQ登录
     * 在View中监听登录操作
     * 完成后进入其它界面，同步用户信息
     */
    override fun loginQQ(activity: Activity) {

        RemoteHelper.loginQQ(activity, "all", mView.getQQListener())

    }

    @SuppressLint("CheckResult")
    override fun getQQInfo(any: Any?, context: Context) {

        /**
         * 获取qq信息后，发起后端的qq登录二部曲
         * 新被观察者（后端api）同时也为上游观察者，执行耗时操作，需要切换到io执行
         * 如果不是第一次登录，则用filter截断第二个api的发送
         */

        mView.showProgress()

        RemoteHelper.getQQUserInfo(any, context)
            .bindToLifecycle(mView)
            .observeOn(Schedulers.io())
            .doOnNext {
                MyApp.globalUser.apply {
                    name = it.name
                    avatar = it.avatar
                }
            }
            .flatMap {
                PreferenceHelper.setLoginWay(PreferenceHelper.wayQQ)
                PreferenceHelper.saveQQLogin(RemoteQQ.getOpenId())
                RemoteHelper.loginQQ()
            }
            .doOnNext {
                if(it.state == 200) {
                    mView.finishLogin()
                    mView.hideProgress()
                }
                MyApp.token = it.result.token
            }
            .filter {
                it.state == 201
            }
            .flatMap {
                RemoteHelper.uploadQQInfo(MyApp.globalUser.name, MyApp.globalUser.avatar)
            }
            .subscribe(object: RemoteObserver<State>() {
                override fun onNext(t: State) {
                    //2001 未调用第一个api（未登录）
                    mView.finishLogin()
                    mView.hideProgress()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    toast("网络好像有、问题")
                    mView.hideProgress()
                }
            })
    }

    @SuppressLint("CheckResult")
    override fun listenFormatCheck(email: Observable<CharSequence>, password: Observable<CharSequence>) {

        Observable.combineLatest(email, password, BiFunction { t1: CharSequence, t2: CharSequence ->
            this.email = t1.toString()
            this.password = t2.toString()

            mView.validateEmail() && t2.isNotEmpty()
        }).subscribe {
            mView.setLoginButtonEnable(it)
        }
    }

}