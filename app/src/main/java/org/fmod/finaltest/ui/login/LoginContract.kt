package org.fmod.finaltest.ui.login

import android.app.Activity
import android.content.Context
import com.tencent.tauth.IUiListener
import io.reactivex.Observable
import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView

interface LoginContract {

    interface View: BaseView<Presenter> {

        fun setLoginButtonEnable(enable: Boolean)

        fun validateEmail(): Boolean

        fun getQQListener(): IUiListener

        fun finishLogin()

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter: BasePresenter {

        fun listenFormatCheck(email: Observable<CharSequence>, password: Observable<CharSequence>)

        fun prepareLogin(context: Context)

        fun login()

        /**
         * 获取token，openid等
         */
        fun loginQQ(activity: Activity)

        /**
         * 获取用户信息
         */
        fun getQQInfo(any: Any?, context: Context)
    }
}