package org.fmod.finaltest.ui.splash

import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.base.abstracts.RemoteObserver
import org.fmod.finaltest.bean.remote.LoginCode
import org.fmod.finaltest.helper.pref.PreferenceHelper
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.networkLog

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

        if(!PreferenceHelper.isLoginOnStart()) {
            mView.noLogin()
            return
        }

        PreferenceHelper.run {
            when(getLoginWay()) {
                wayEmail -> {
                    val email = getEmail()
                    val password = getEmailPassword()
                    if(email.isNullOrEmpty() || password.isNullOrEmpty()) {
                        mView.noLogin()
                        return
                    }
                    networkLog("Splash login, email:$email, pw:$password")
                    RemoteHelper.login(email, password)
                        .bindToLifecycle(mView)
                        .doOnNext {
                            if(it.state == 401)
                                mView.noLogin()
                        }
                        .filter {
                            it.state == 200
                        }
                        .subscribe(object : RemoteObserver<LoginCode>() {
                            override fun onNext(t: LoginCode) {
                                mView.finishLogin(true)
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                mView.finishLogin(false)
                            }
                        })
                }
                wayQQ -> {
                    val openId = getQQOpenId()
                    if(openId.isNullOrEmpty()) {
                        mView.noLogin()
                        return
                    }

                    RemoteHelper.loginQQ(openId)
                        .bindToLifecycle(mView)
                        .subscribe(object : RemoteObserver<LoginCode>() {

                            override fun onNext(t: LoginCode) {
                                //因为已经有了qq登录的标志，所以通过qq自动登录时，肯定是非第一次登录，不需判断state
                                mView.finishLogin(true)
                            }

                            override fun onError(e: Throwable) {
                                super.onError(e)
                                mView.finishLogin(false)
                            }
                        })
                }
                else -> {
                    log("Not all the existing login way")
                }
            }
        }


    }

}