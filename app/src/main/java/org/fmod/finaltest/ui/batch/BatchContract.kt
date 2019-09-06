package org.fmod.finaltest.ui.batch

import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView
import org.fmod.finaltest.bean.DealItem

interface BatchContract {
    interface View: BaseView<Presenter> {

    }

    interface Presenter: BasePresenter {
        fun exportExcel(
            filename: String,
            title: String,
            headers: List<String>,
            data: List<DealItem>
        )
    }
}