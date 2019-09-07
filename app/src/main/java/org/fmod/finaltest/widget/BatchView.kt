package org.fmod.finaltest.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.batch_item.view.*
import org.fmod.finaltest.R
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.util.toplevel.log

class BatchView(layoutInflater: LayoutInflater, container: ViewGroup?) {

    val view: View = layoutInflater.inflate(R.layout.batch_item, container, false)

    fun bind(item: DealItem, checkCallback: (DealItem, Boolean)->Unit) {

        view.run {
            batch_icon.backgroundTintList = item.bigKind.bgColorRes
            batch_icon.setImageResource(item.bigKind.iconResId)
            batch_bigKind.text = item.bigKind.name
            batch_money.text = if(item.isIncome) {
                "${item.money}"
            } else {
                "-${item.money}"
            }

            batch_check.isChecked = item.selected
            batch_check.setOnClickListener {
                item.selected = batch_check.isChecked
                checkCallback(item, batch_check.isChecked)
            }
            setOnClickListener {
                batch_check.isChecked = !batch_check.isChecked
                item.selected = batch_check.isChecked
                checkCallback(item, batch_check.isChecked)
            }

        }
    }

}