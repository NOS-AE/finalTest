package org.fmod.finaltest.widget

import android.widget.TextView
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R

class IncomeSwitch(
    private val incomeView: TextView,
    private val expenditureView: TextView,
    private val switchCallback: (Boolean)->Unit,
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

        incomeView.setOnClickListener {

            if(isIncome) {
                return@setOnClickListener
            }

            incomeView.setTextColor(selectedColor)
            expenditureView.setTextColor(defaultColor)
            isIncome = true
            switchCallback(isIncome)
        }

        expenditureView.setOnClickListener {

            if(!isIncome){
                return@setOnClickListener
            }

            incomeView.setTextColor(defaultColor)
            expenditureView.setTextColor(selectedColor)
            isIncome = false
            switchCallback(isIncome)
        }
    }


}