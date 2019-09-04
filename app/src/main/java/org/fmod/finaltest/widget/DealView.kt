package org.fmod.finaltest.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_list_item.view.*
import org.fmod.finaltest.R
import org.fmod.finaltest.bean.DealItem

class DealView(layoutInflater: LayoutInflater, container: ViewGroup?) {

    val view: View = layoutInflater.inflate(R.layout.main_list_item, container, false)

    fun bind(item: DealItem) {
        view.run {
            item_icon.backgroundTintList = item.bigKind.bgColorRes
            item_icon.setImageResource(item.bigKind.iconResId)
            item_bigKind.text = item.bigKind.name
            item_money.text = item.money.toString()
        }
    }
}