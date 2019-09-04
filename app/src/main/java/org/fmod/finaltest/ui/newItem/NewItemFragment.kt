package org.fmod.finaltest.ui.newItem


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bigkoo.pickerview.view.TimePickerView
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_new_item.*
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R
import org.fmod.finaltest.adapter.BigKindAdapter
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.ItemMore
import org.fmod.finaltest.bean.bus.ItemMoreDealItem
import org.fmod.finaltest.ui.itemMore.ItemMoreFragment
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.widget.PickerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import java.util.concurrent.TimeUnit

class NewItemFragment : BaseFragment(), NewItemContract.View {

    private lateinit var presenter: NewItemContract.Presenter

    private lateinit var timePicker: TimePickerView

    private val dealItem = DealItem()
    private var money = "0"

    companion object{
        fun newInstance() = NewItemFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutId() = R.layout.fragment_new_item


    @SuppressLint("CheckResult")
    override fun setListener() {

        newItem_calendar.clicks()
            .bindToLifecycle(this)
            .throttleFirst(1L, TimeUnit.SECONDS)
            .subscribe {
                timePicker.show()
            }

        presenter.listenMoneyChange(
            newItem_money_input.textChangeEvents(),
            newItem_money_input.afterTextChangeEvents()
        )

        newItem_cancel.clicks()
            .bindToLifecycle(this)
            .throttleFirst(1L, TimeUnit.SECONDS)
            .subscribe {
                pop()
            }

        newItem_ok.clicks()
            .bindToLifecycle(this)
            .throttleFirst(1L, TimeUnit.SECONDS)
            .subscribe {
                dealItem.money = this.money.toDouble()
                presenter.createNewItem(dealItem)
                pop()
            }

        newItem_income.clicks()
            .bindToLifecycle(this)
            .subscribe {
                newItem_expenditure.setTextColor(MyApp.appContext.getColor(R.color.newItemDark))
                newItem_income.setTextColor(MyApp.appContext.getColor(R.color.colorTheme))
                dealItem.isIncome = true
            }

        newItem_expenditure.clicks()
            .bindToLifecycle(this)
            .subscribe {
                newItem_income.setTextColor(MyApp.appContext.getColor(R.color.newItemDark))
                newItem_expenditure.setTextColor(MyApp.appContext.getColor(R.color.colorTheme))
                dealItem.isIncome = false
            }

        newItem_remarks.clicks()
            .bindToLifecycle(this)
            .throttleFirst(1L, TimeUnit.SECONDS)
            .subscribe {
                startFragment(ItemMoreFragment.newInstance())
                EventBus.getDefault().postSticky(ItemMoreDealItem(dealItem))
            }

    }


    override fun initView() {

        newItem_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0,statusBarHeight,0,0)
        }

        timePicker = PickerView.getTimePicker(requireContext(), Calendar.getInstance()) {
            dealItem.dateNum = it
        }

    }

    override fun afterInitView() {
        presenter.loadBigKinds()
    }

    override fun onDestroyView() {
       EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    /* Bus */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun handleItemMore(item: ItemMore) {

        log(item.remarks)
        dealItem.run {
            remarks = item.remarks
        }

    }

    /* NewItemContract.View Methods */

    override fun injectPresenter(presenter: NewItemContract.Presenter) {
        this.presenter = presenter
    }

    @SuppressLint("CheckResult")
    override fun showBigKinds(kindList: ArrayList<BigKind>) {

        val adapter = BigKindAdapter(kindList,
            {
                newItem_icon.backgroundTintList = it.bgColorRes
                newItem_icon.setImageResource(it.iconResId)
                newItem_bigKind.text = it.name
                dealItem.bigKind = it.copy()
            },
            0)
        /*adapter.selectObservable
            .bindToLifecycle(this)
            .subscribe {

            }*/

        newItem_kind.adapter = adapter
        newItem_kind.layoutManager = GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL, false)

    }

    override fun showMoney(money: CharSequence) {
        this.money = money.toString()
        newItem_money.text = money
    }

    override fun modifyMoneyInput(money: CharSequence) {
        newItem_money.text = money
        newItem_money_input.setText(money)
        newItem_money_input.setSelection(money.length)
    }


}
