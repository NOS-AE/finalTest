package org.fmod.finaltest.ui.mine

import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView

interface MineContract {

    interface View: BaseView<Presenter> {

        fun finishChangePw(success: Boolean)

        fun changePwFormatFail()
    }

    interface Presenter: BasePresenter {
        fun changePassword(old: String?, new: String?)
    }

}