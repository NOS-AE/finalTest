package org.fmod.finaltest.util.toplevel

import java.util.*

const val MS_PER_DAY: Long = 1000 * 60 * 60 * 24

fun isTheSameDay(d1: Calendar, d2: Calendar): Boolean{
    return d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR) &&
            d1.get(Calendar.DAY_OF_YEAR) == d2.get(Calendar.DAY_OF_YEAR)
}