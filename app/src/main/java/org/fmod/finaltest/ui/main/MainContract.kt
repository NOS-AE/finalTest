package org.fmod.finaltest.ui.main

import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView
import org.fmod.finaltest.bean.DealItem

interface MainContract {
    interface View: BaseView<Presenter> {
        fun showItems(items: ArrayList<DealItem>)
    }

    interface Presenter: BasePresenter {
        fun loadItems()
    }
}