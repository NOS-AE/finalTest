package org.fmod.finaltest.ui.batch

import android.annotation.SuppressLint
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.util.excel.ExcelMaker
import org.fmod.finaltest.util.toplevel.poiLog
import org.fmod.finaltest.util.toplevel.toast

class BatchPresenter(
    private val mView: BatchContract.View
): BatchContract.Presenter {

    override fun start() {

    }

    @SuppressLint("CheckResult")
    override fun exportExcel(
        filename: String,
        title: String,
        headers: List<String>,
        data: List<DealItem>
    ) {
        ExcelMaker.newInstance()
            .createSheet(title, headers, data)
            .export(filename)
            .bindToLifecycle(mView)
            .subscribe({
                toast("成功导出到$it")
            }, {
                poiLog("导出Excel异常: $it")
                toast("导出失败")
            })
    }

    @SuppressLint("CheckResult")
    fun syncItem(dealItem: DealItem) {
        val param = HashMap<String, String>().apply {
            put("income", dealItem.isIncome.toString())
            put("money", dealItem.money.toString())
            put("currency", "¥")
            put("type", dealItem.type)
            put("remark", dealItem.remarks)
            put("token", MyApp.token)
        }
        RemoteHelper.syncDealItem(param)
            .subscribe {
                toast("已同步到网络")
            }
    }
}