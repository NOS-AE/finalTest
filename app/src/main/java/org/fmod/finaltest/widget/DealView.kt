package org.fmod.finaltest.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.deal_item.view.*
import org.fmod.finaltest.R
import org.fmod.finaltest.bean.DealItem

class DealView(layoutInflater: LayoutInflater, container: ViewGroup?) {

    val view: View = layoutInflater.inflate(R.layout.deal_item, container, false)

    fun bind(item: DealItem, clickCallback:(DealItem)->Unit) {
        view.run {
            item_icon.backgroundTintList = item.bigKind.bgColorRes
            item_icon.setImageResource(item.bigKind.iconResId)
            item_bigKind.text = item.bigKind.name
            item_money.text = if(item.isIncome) {
                "${item.money}"
            } else {
                "-${item.money}"
            }
            setOnClickListener {
                clickCallback(item)
            }
        }
    }
}