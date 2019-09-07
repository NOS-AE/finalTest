package org.fmod.finaltest.ui.statistics


import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_sta.*
import org.fmod.finaltest.MyApp

import org.fmod.finaltest.R
import org.fmod.finaltest.adapter.StaAdapter
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.StaItem
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.widget.IncomeSwitch
import kotlin.math.exp

/**
 * A simple [Fragment] subclass.
 */
class StaFragment : BaseFragment() {

    companion object {
        fun newInstance(items: ArrayList<DealItem>) =
            StaFragment().apply {
                dealItems = items
            }
    }

    private lateinit var dealItems: ArrayList<DealItem>

    private lateinit var incomeSwitch: IncomeSwitch

    private lateinit var staAdapter: StaAdapter

    //<大分类，金额>
    private var incomeData = HashMap<BigKind, Double>()
    private var expenditureData = HashMap<BigKind, Double>()

    //lateinit var presenter

    override fun getLayoutId() = R.layout.fragment_sta

    override fun setListener() {

    }

    override fun initView() {

        sta_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight, 0, 0)
        }

        incomeSwitch = IncomeSwitch(sta_income, sta_expenditure, {
            setPie(it)
            setList(it)
        })

        sta_pie.run {
            setUsePercentValues(true)
            centerText = "统计"
            isDrawHoleEnabled = true
            setHoleColor(MyApp.appContext.getColor(R.color.white))
            isRotationEnabled = false
            description.isEnabled = false
            legend.apply {
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                verticalAlignment = Legend.LegendVerticalAlignment.CENTER
                orientation = Legend.LegendOrientation.VERTICAL
                yEntrySpace = 2f
            }

        }

        sta_list.layoutManager = LinearLayoutManager(context)
        staAdapter = StaAdapter(ArrayList())


        getData()
        setPie(false)
        setList(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.sta_switch -> {

            }
        }
        return true
    }

    private fun getData() {
        dealItems.forEach {

            if(it.isIncome) {
                incomeData[it.bigKind] = incomeData.getOrDefault(it.bigKind, 0.0) + it.money
            } else {
                expenditureData[it.bigKind] = expenditureData.getOrDefault(it.bigKind, 0.0) + it.money
            }

        }
    }

    private fun setPie(isIncome: Boolean) {

        val data = if(isIncome) incomeData else expenditureData

        val entries = ArrayList<PieEntry>()

        data.forEach {
            entries.add(PieEntry(it.value.toFloat(), it.key.name))
        }
        val dataSet = PieDataSet(entries,"")

        dataSet.setDrawIcons(false)
        val colors = MyApp.appContext.resources.getIntArray(R.array.icon_colors)
        //dataSet.color = MyApp.appContext.getColor(R.color.colorTheme)
        dataSet.colors = colors.toList()
        dataSet.label

        dataSet.sliceSpace = 1.5f
        sta_pie.data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(11f)
            setValueTextColor(MyApp.appContext.getColor(R.color.white))
            getDataSetByLabel("",false)
        }

        sta_pie.invalidate()
    }

    private fun setList(isIncome: Boolean) {

        val data = if(isIncome) incomeData else expenditureData

        if(data.isEmpty()) {
            staAdapter.switchList(ArrayList())
            return
        }

        val staList = ArrayList<StaItem>()
        var totalMoney = 0.0

        data.forEach {
            totalMoney += it.value
            staList.add(StaItem(it))
        }
        staList.forEach {
            it.percentage = it.entry.value / totalMoney
        }

        sta_list.adapter = StaAdapter(staList)
    }

}
