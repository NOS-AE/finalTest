package org.fmod.finaltest.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sta_item.view.*
import org.fmod.finaltest.R
import org.fmod.finaltest.bean.StaItem
import java.text.NumberFormat

class StaView(layoutInflater: LayoutInflater, container: ViewGroup?) {

    companion object {
        private val percentageFormat = NumberFormat.getPercentInstance().apply {
            minimumFractionDigits = 1
        }
    }

    val view: View = layoutInflater.inflate(R.layout.sta_item, container, false)

    fun bind(item: StaItem) {
        view.run {
            val bigKind = item.entry.key

            staItem_icon.backgroundTintList = bigKind.bgColorRes
            staItem_icon.setImageResource(bigKind.iconResId)
            staItem_bigKind.text = bigKind.name

            staItem_money.text = item.entry.value.toString()
            staItem_percent.text = percentageFormat.format(item.percentage)
            staItem_progress.progress = (item.percentage * 100).toInt()
        }
    }

}