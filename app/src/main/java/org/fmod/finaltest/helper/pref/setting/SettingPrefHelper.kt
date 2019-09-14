package org.fmod.finaltest.helper.pref.setting

import android.content.Context
import android.content.SharedPreferences
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.helper.pref.PrefHelper

class SettingPrefHelper<T>(default: T): PrefHelper<T>(default) {
    override fun getSharePreference(): SharedPreferences = spf

    companion object {
        private val spf by lazy {
            MyApp.appContext.getSharedPreferences("setting", Context.MODE_PRIVATE)
        }

        fun clear() {
            spf.edit().clear().apply()
        }
    }
}