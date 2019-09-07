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
import org.fmod.finaltest.R
import org.fmod.finaltest.adapter.BigKindAdapter
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.ItemMore
import org.fmod.finaltest.bean.bus.ItemMoreDealItem
import org.fmod.finaltest.bean.bus.NewItemDealItem
import org.fmod.finaltest.ui.itemMore.ItemMoreFragment
import org.fmod.finaltest.ui.main.MainFragment.Companion.RES_ITEM_OK
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.widget.IncomeSwitch
import org.fmod.finaltest.widget.PickerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 处理New Item和Modify Item
 */

class NewItemFragment : BaseFragment(), NewItemContract.View {

    private lateinit var presenter: NewItemContract.Presenter

    private lateinit var timePicker: TimePickerView

    private lateinit var dealItem: DealItem

    private var isNewItem = false

    private var newItem = DealItem()

    private lateinit var incomeSwitch: IncomeSwitch

    private lateinit var money: String

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
                newItem.isIncome = incomeSwitch.isIncome
                if (this::money.isInitialized)
                    newItem.money = money.toDouble()
                if(isNewItem) {
                    presenter.createNewItem(newItem)
                    pop()
                } else {
                    dealItem.set(newItem)
                    dealItem.update(dealItem.id)
                    popWithResult()
                }
            }

        newItem_remarks.clicks()
            .bindToLifecycle(this)
            .throttleFirst(1L, TimeUnit.SECONDS)
            .subscribe {
                startFragment(ItemMoreFragment.newInstance())
                EventBus.getDefault().postSticky(ItemMoreDealItem(newItem))
            }

    }


    override fun initView() {

        newItem_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0,statusBarHeight,0,0)
        }

        timePicker = PickerView.getTimePicker(requireContext(), Calendar.getInstance()) {
            newItem.dateNum = it
        }

        /* Init View by newItem */
        timePicker.setDate(Calendar.getInstance().apply {
            time = newItem.dateNum
        })

        incomeSwitch = IncomeSwitch(newItem_income, newItem_expenditure, {

        })

        newItem_money.text = newItem.money.toString()

        newItem_money_input.setText(newItem.money.toString())

        newItem_bigKind.text = newItem.type

        newItem_icon.run {
            backgroundTintList = newItem.bigKind.bgColorRes
            setImageResource(newItem.bigKind.iconResId)
        }

        presenter.loadBigKinds(newItem.bigKind)
    }

    override fun afterInitView() {

    }

    override fun onDestroyView() {
       EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun handleItemMore(item: ItemMore) {

        newItem.run {
            remarks = item.remarks
        }
    }

    /*
        创建Item：接收到的NewItemDealItem内容为空
        修改Item：接收到的NewItemDealItem内容为待修改Item
     */
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public fun handleItem(item: NewItemDealItem) {
        EventBus.getDefault().removeStickyEvent(item)
        isNewItem = if(item.item == null) {
            true
        } else {
            dealItem = item.item
            newItem.set(dealItem)
            false
        }
    }

    private fun popWithResult() {
        setFragmentResult(RES_ITEM_OK, null)
        pop()
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
                newItem.bigKind = it
            },
            newItem.bigKind)

        newItem_kind.adapter = adapter
        newItem_kind.layoutManager = GridLayoutManager(context,2,GridLayoutManager.HORIZONTAL, false)

    }

    override fun showMoney(money: String) {
        this.money = money
        newItem_money.text = money
    }

    override fun modifyMoneyInput(money: CharSequence) {
        newItem_money.text = money
        newItem_money_input.setText(money)
        newItem_money_input.setSelection(money.length)
    }


}
