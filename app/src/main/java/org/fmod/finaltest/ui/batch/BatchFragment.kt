package org.fmod.finaltest.ui.batch


import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_batch.*

import org.fmod.finaltest.R
import org.fmod.finaltest.adapter.BatchAdapter
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.bus.BatchDealItem
import org.fmod.finaltest.ui.main.MainFragment
import org.fmod.finaltest.util.excel.ExcelMaker
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.poiLog
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.util.toplevel.toast
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BatchFragment : BaseFragment(), BatchContract.View {

    companion object {
        fun newInstance() = BatchFragment()
    }

    lateinit var presenter: BatchContract.Presenter

    private lateinit var dealItems: ArrayList<DealItem>

    private lateinit var adapter: BatchAdapter

    private var selectedCount = 0

    private val excelHeader = listOf("大分类","交易日期","收支类型","金额","备注")

    override fun onCreate(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId() = R.layout.fragment_batch

    override fun setListener() {

        batch_toolbar.setNavigationOnClickListener {
            pop()
        }

        batch_delete.setOnClickListener {
            deleteSelectedItems()
        }

        batch_moveTo.setOnClickListener {

        }

        batch_export.setOnClickListener {

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            val filename = "${Environment.getExternalStorageDirectory().absolutePath}/${format.format(
                Date()
            )}.xls"

            presenter.exportExcel(filename, "账本", excelHeader, dealItems)
        }

    }

    override fun initView() {

        batch_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight, 0, 0)
        }

        adapter = BatchAdapter(dealItems) { _, isCheck ->
            if(isCheck) {
                selectedCount++
            } else {
                selectedCount--
            }
            val title = "已选择${selectedCount}项"
            batch_toolbar.title = title
        }
        batch_list.adapter = adapter
        batch_list.layoutManager = LinearLayoutManager(context)
    }

    override fun onBackPressedSupport(): Boolean {
        log("pop with result")
        //popWithResult()
        return true
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        setFragmentResult(MainFragment.RES_BATCH_OK, null)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public fun handleDealItems(items: BatchDealItem) {
        EventBus.getDefault().removeStickyEvent(items)

        dealItems = items.items
        dealItems.forEach {
            if(it.selected) it.selected = false
        }
    }

    private fun getSelectedItems() = dealItems.filter {
        it.selected
    }

    private fun deleteSelectedItems() {
        dealItems.removeAll {
            if(it.selected) {
                it.delete()
                true
            } else {
                false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /* BatchContract.View Methods */

    override fun injectPresenter(presenter: BatchContract.Presenter) {
        this.presenter = presenter
    }


}
