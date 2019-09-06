package org.fmod.finaltest.widget

import android.widget.TextView
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R

class IncomeSwitch(
    private val incomeView: TextView,
    private val expenditureView: TextView,
    var isIncome: Boolean = false
) {

    private val selectedColor = MyApp.appContext.getColor(R.color.colorTheme)
    private val defaultColor = MyApp.appContext.getColor(R.color.newItemDark)

    init {
        if(isIncome) {
            incomeView.setTextColor(selectedColor)
            expenditureView.setTextColor(defaultColor)
        } else {
            incomeView.setTextColor(defaultColor)
            expenditureView.setTextColor(selectedColor)
        }
    }

    fun setIncome() {
        if(isIncome) {
            return
        }

        incomeView.setTextColor(selectedColor)
        expenditureView.setTextColor(defaultColor)
        isIncome = true
    }

    fun setExpenditure() {
        if(!isIncome){
            return
        }

        incomeView.setTextColor(defaultColor)
        expenditureView.setTextColor(selectedColor)
        isIncome = false
    }

}