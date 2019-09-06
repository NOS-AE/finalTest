package org.fmod.finaltest.ui.newItem

import android.annotation.SuppressLint
import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.widget.TextViewAfterTextChangeEvent
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.bus.MainDealItem
import org.fmod.finaltest.helper.local.LocalHelper
import org.fmod.finaltest.util.toplevel.log
import org.greenrobot.eventbus.EventBus

class NewItemPresenter(
    private val mView: NewItemContract.View
): NewItemContract.Presenter {

    private val moneyMaxInt = 7
    private val moneyMaxDec = 2
    private var moneyInput = ""

    private var moneyModify = false

    override fun start() {

    }

    @SuppressLint("CheckResult")
    override fun loadBigKinds(selectedKind: BigKind) {

        val finalSelected = MyApp.bigKinds.find {
            it.selected
        }
        finalSelected?.selected = false
        selectedKind.selected = true
        mView.showBigKinds(MyApp.bigKinds)

    }



    @SuppressLint("CheckResult")
    override fun listenMoneyChange(
        onChange: InitialValueObservable<TextViewTextChangeEvent>,
        afterChange: InitialValueObservable<TextViewAfterTextChangeEvent>
    ) {

        onChange.skipInitialValue()
            .bindToLifecycle(mView)
            .subscribe {

            moneyInput = it.text.toString()
            moneyModify = true

            var res = when{
                moneyInput.contains(".") &&
                        (moneyInput.length - 1 - moneyInput.indexOf(".") > moneyMaxDec) -> {
                    //限制小数位数
                    moneyInput.substring(0,moneyInput.indexOf(".") + moneyMaxDec + 1)
                }
                moneyInput.length > moneyMaxInt -> {
                    //限制整数位数
                    moneyInput.subSequence(0, moneyMaxInt)
                }
                else -> {
                    moneyModify = false
                    moneyInput
                }
            }

            /*if(moneyModify){
                log("change modify $res")
                afterMoneyModify = true*/
            if(moneyModify) {
                mView.modifyMoneyInput(res)
            }
            else {
                if(res.isEmpty()) res = "0"
                mView.showMoney(res.toString())
            }

            /*}else {
                log("change showMoney $res")
                mView.showMoney("$res\$")
            }*/
        }

        afterChange.skipInitialValue()
            .bindToLifecycle(mView)
            .subscribe {

            var res = when {
                moneyInput == "." -> {
                    //禁止无整数下输入小数点
                    ""
                }
                moneyInput.length == 2 && moneyInput[0] == '0' && moneyInput[1] != '.' -> {
                    //禁止无效零
                    "0"
                }
                else -> {
                    moneyModify = false
                    moneyInput
                }
            }

            /*if(moneyModify){
                log("after modify $res")
                afterMoneyModify = true*/
            if(moneyModify) {
                mView.modifyMoneyInput(res)
            }
            else {
                if(res.isEmpty()) res = "0"
                mView.showMoney(res)
            }
            /*}else {
                log("after showMoney $res")
                mView.showMoney("$res\$")
            }*/
        }
    }

    override fun createNewItem(item: DealItem) {

        log("post new item $item")
        EventBus.getDefault().post(MainDealItem(item))
        item.save()

    }

}