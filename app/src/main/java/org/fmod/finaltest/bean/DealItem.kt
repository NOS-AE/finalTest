package org.fmod.finaltest.bean

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.lang.StringBuilder
import java.util.*
import kotlin.properties.Delegates

class DealItem: LitePalSupport(){

    @delegate:Column(ignore = true)
    var bigKind by Delegates.observable(BigKind()) { _, _, new ->
        type = new.name
    }
    var dateNum = Date()  //交易日期
    var isIncome = false    //是否收入
    var type: String = bigKind.name   //交易类型
    var remarks = ""    //备注
    var money = 0.0  //金额

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