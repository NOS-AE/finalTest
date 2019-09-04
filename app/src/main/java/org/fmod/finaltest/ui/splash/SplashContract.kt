package org.fmod.finaltest.ui.splash

import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView

interface SplashContract {

    interface View: BaseView<Presenter> {
        /**
         * @param success 是否登录成功，否则请检查网络
         */
        fun finishLogin(success: Boolean)

        fun noLogin()
    }

    interface Presenter: BasePresenter {
        fun tryLogin()
    }
}