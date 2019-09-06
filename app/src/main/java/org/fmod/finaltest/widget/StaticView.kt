package org.fmod.finaltest.widget

import android.view.ViewGroup
import kotlinx.android.synthetic.main.all_statistics_container.view.*
import org.fmod.finaltest.bean.DealItem
import java.util.*
import kotlin.collections.ArrayList

class StaticView(private val parent: ViewGroup) {

    lateinit var items: ArrayList<DealItem>

    private val month = Calendar.getInstance().get(Calendar.MONTH) + 1

    init {
        parent.run {
            val hint1 = "${month}月收入"
            main_income_month.text = hint1
            val hint2 = "${month}月支出"
            main_expenditure_month.text = hint2
        }
    }

    fun notifyStaticsChange() {
        var income = 0.0
        var expenditure = 0.0
        for(i in items) {
            if(i.isIncome) {
                income += i.money
            } else {
                expenditure += i.money
            }
        }
        parent.run {
            main_income.text = income.toString()
            main_expenditure.text = expenditure.toString()
        }
    }

}