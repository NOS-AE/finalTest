package org.fmod.finaltest.bean

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class BigKind(
    var bgColorRes: ColorStateList? = ContextCompat.getColorStateList(MyApp.appContext, R.color.colorTheme),
    var iconResId: Int = R.drawable.ic_menu_black_24dp,
    var name: String = "通用"
): LitePalSupport() {

    @Column(ignore = true)
    var selected = false

}