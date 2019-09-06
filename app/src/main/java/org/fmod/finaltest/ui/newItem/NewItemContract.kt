package org.fmod.finaltest.ui.newItem

import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.widget.TextViewAfterTextChangeEvent
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.DealItem

interface NewItemContract {

    interface View: BaseView<Presenter> {
        fun showBigKinds(kindList: ArrayList<BigKind>)

        fun modifyMoneyInput(money: CharSequence)

        fun showMoney(money: String)
    }

    interface Presenter: BasePresenter {

        fun loadBigKinds(selectedKind: BigKind)

        fun listenMoneyChange(
            onChange: InitialValueObservable<TextViewTextChangeEvent>,
            afterChange: InitialValueObservable<TextViewAfterTextChangeEvent>
        )

        fun createNewItem(item: DealItem)
    }
}