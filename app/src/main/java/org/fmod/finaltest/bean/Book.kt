package org.fmod.finaltest.bean

import android.content.res.ColorStateList
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R
import org.litepal.crud.LitePalSupport

class Book: LitePalSupport() {

    var bgColorRes: ColorStateList = MyApp.appContext.getColorStateList(R.color.colorTheme)

    val name = "通用账本"
}