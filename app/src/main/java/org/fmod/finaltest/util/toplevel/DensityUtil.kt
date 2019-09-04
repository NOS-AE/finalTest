package org.fmod.finaltest.util.toplevel

import android.content.res.Resources

val scale = Resources.getSystem().displayMetrics.density

var statusBarHeight = 0

fun px2dp(value: Float): Int{
    return (scale * value + 0.5).toInt()
}

fun dp2px(value: Float): Int{
    return (scale * value + 0.5).toInt()
}