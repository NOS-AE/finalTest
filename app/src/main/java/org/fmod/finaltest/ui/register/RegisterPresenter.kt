package org.fmod.finaltest.ui.register

import android.annotation.SuppressLint
import com.jakewharton.rxbinding3.InitialValueObservable
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.fmod.finaltest.base.abstracts.RemoteObserver
import org.fmod.finaltest.bean.remote.BaseRes
import org.fmod.finaltest.bean.remote.State
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.toast

class RegisterPresenter(
    private val mView: RegisterContract.View
): RegisterContract.Presenter {

    private lateinit var email: String
    private lateinit var code: String
    private lateinit var password: String

    override fun start() {

    }

    override fun sendCode(email: String?) {
        email ?: return

        this.email = email

        RemoteHelper.sendCode(email)
            .bindToLifecycle(mView)
            .subscribe(object: RemoteObserver<State>() {
                override fun onNext(t: State) {
                    log(t.toString())
                    toast("验证码已发送，请在邮箱内查看")
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    toast("发送失败，请检查网络")
                }
            })
    }

    @SuppressLint("CheckResult")
    override fun checkEditText(
        code: InitialValueObservable<CharSequence>,
        password: InitialValueObservable<CharSequence>
    ) {
        Observable.combineLatest(code, password, BiFunction { t1: CharSequence, t2: CharSequence ->

            this.code = t1.toString()
            this.password = t2.toString()

            t1.length == 4 && t2.isNotEmpty()
        })
            .bindToLifecycle(mView)
            .subscribe {
                mView.setRegisterButtonEnable(it)
            }

    }

    override fun register() {

        RemoteHelper.register(email, code, password)
            .bindToLifecycle(mView)
            .subscribe(object: RemoteObserver<State>() {
                override fun onNext(t: State) {
                    mView.finishRegister()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    toast("注册失败，请检查网络")
                }
            })

    }



}
