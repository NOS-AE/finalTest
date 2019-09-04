package org.fmod.finaltest.ui.register

import com.jakewharton.rxbinding3.InitialValueObservable
import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView

interface RegisterContract {

    interface View: BaseView<Presenter> {
        fun finishRegister()

        fun setRegisterButtonEnable(enable: Boolean)
    }

    interface Presenter: BasePresenter {

        fun sendCode(email: String?)

        fun checkEditText(code: InitialValueObservable<CharSequence>, password: InitialValueObservable<CharSequence>)

        fun register()

    }

}