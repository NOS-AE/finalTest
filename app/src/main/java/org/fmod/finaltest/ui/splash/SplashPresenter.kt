package org.fmod.finaltest.ui.splash

import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.base.abstracts.RemoteObserver
import org.fmod.finaltest.bean.remote.BaseRes
import org.fmod.finaltest.bean.remote.Login
import org.fmod.finaltest.helper.pref.user.LoginWay
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.manager.DataManager

class SplashPresenter(
    private val mView: SplashContract.View
): SplashContract.Presenter {

    override fun start() {

    }

    override fun tryLogin() {

        if(!RemoteHelper.isNetWorkConnected()){
            mView.finishLogin(false)
            return
        }

        if(!DataManager.autoLogin || DataManager.loginWay == LoginWay.NO_LOGIN) {
            mView.noLogin()
            return
        }

        when(DataManager.loginWay) {
            LoginWay.EMAIL -> loginEmail()
            LoginWay.QQ -> loginQQ()
            LoginWay.WECHAT -> loginWechat()
            LoginWay.WEIBO -> loginWeibo()
        }

    }

    private fun loginEmail() {
        val email = DataManager.email
        val password = DataManager.password
        RemoteHelper.login(email, password)
            .bindToLifecycle(mView)
            .doOnNext {
                if(it.state == 401) {
                    DataManager.loginWay = LoginWay.NO_LOGIN
                    mView.noLogin()
                }
            }
            .filter {
                it.state == 200
            }
            .subscribe(object : RemoteObserver<BaseRes<Login>>() {
                override fun onNext(t: BaseRes<Login>) {
                    MyApp.token = t.result.token
                    mView.finishLogin(true)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mView.finishLogin(false)
                }
            })
    }

    private fun loginQQ() {
        val id = DataManager.qqId
        RemoteHelper.loginQQ(id)
            .bindToLifecycle(mView)
            .subscribe(object : RemoteObserver<BaseRes<Login>>() {
                override fun onNext(t: BaseRes<Login>) {
                    //肯定不是第一次登录，不需判断state
                    MyApp.token = t.result.token
                    mView.finishLogin(true)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    mView.finishLogin(false)
                }
            })
    }

    private fun loginWechat() {

    }

    private fun loginWeibo() {

    }

}