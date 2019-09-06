package org.fmod.finaltest.bean

import org.fmod.finaltest.MyApp
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.lang.StringBuilder
import java.util.*
import kotlin.properties.Delegates

open class DealItem: LitePalSupport(){

    @delegate:Column(ignore = true)
    var bigKind by Delegates.observable(MyApp.bigKinds[0]) { _, _, new ->
        type = new.name
    }
    @Column(ignore = true)
    var selected = false
    @Column(unique = true)
    var id = 0L
    var dateNum = Date()  //交易日期
    var isIncome = false    //是否收入
    var remarks = ""    //备注
    var money = 0.0  //金额
    /*Observer*/
    var type: String = bigKind.name   //交易类型

    fun set(toCopy: DealItem) {
        bigKind = toCopy.bigKind
        dateNum = toCopy.dateNum
        isIncome = toCopy.isIncome
        remarks = toCopy.remarks
        money = toCopy.money
    }

    override fun toString(): String {
        return StringBuilder().apply {
            appendln(dateNum)
            appendln(isIncome)
            appendln(type)
            appendln(remarks)
            appendln(money)
        }.toString()
    }

}