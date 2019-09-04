package org.fmod.finaltest.ui.itemMore



import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_item_more.*
import org.fmod.finaltest.R
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.ItemMore
import org.fmod.finaltest.bean.bus.ItemMoreDealItem
import org.fmod.finaltest.util.toplevel.dp2px
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ItemMoreFragment : BaseFragment() {

    companion object {
        fun newInstance() = ItemMoreFragment()
    }

    /*
        接收DealItemBean来初始化ItemMoreBean
        “确定”后发送ItemMoreBean（非sticky而且NewItem和ModifyItem之间只有一个register，能确保不干扰）
     */
    private lateinit var item: ItemMore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutId() = R.layout.fragment_item_more

    @SuppressLint("CheckResult")
    override fun setListener() {

        itemMore_confirm.clicks()
            .bindToLifecycle(this)
            .subscribe {
                item.remarks = itemMore_remarks.text.toString()
                EventBus.getDefault().post(item)
                pop()
            }

        itemMore_toolbar.setNavigationOnClickListener {
            pop()
        }
    }

    override fun initView() {

        itemMore_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight, 0, 0)
        }

        itemMore_remarks.setText(item.remarks)

        itemMore_remarks.setPaddings(dp2px(5f), 0, dp2px(5f), 0)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public fun handleDealItem(item: ItemMoreDealItem) {
        EventBus.getDefault().removeStickyEvent(item)
        val deal = item.item
        this.item = ItemMore().apply {
            remarks = deal.remarks
        }
    }

}
