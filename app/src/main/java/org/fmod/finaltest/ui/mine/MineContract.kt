package org.fmod.finaltest.ui.mine

import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView
import org.fmod.finaltest.bean.remote.UserInfo

interface MineContract {

    interface View: BaseView<Presenter> {

        fun finishChangePw(success: Boolean)

        fun changePwFormatFail()

        fun showUserInfo(info: UserInfo)

        fun changeNameFail()
    }

    interface Presenter: BasePresenter {
        fun changePassword(old: String?, new: String?)

        fun changeName(name: String?)

        fun loadInfo()
    }

}