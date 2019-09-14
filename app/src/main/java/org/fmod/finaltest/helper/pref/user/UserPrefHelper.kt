package org.fmod.finaltest.helper.pref.user

import android.content.Context
import android.content.SharedPreferences
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.helper.pref.PrefHelper

class UserPrefHelper<T>(default: T): PrefHelper<T>(default) {

    override fun getSharePreference(): SharedPreferences = spf

    companion object {
        private val spf by lazy {
            MyApp.appContext.getSharedPreferences("user", Context.MODE_PRIVATE)
        }

        fun clear() {
            spf.edit().clear().apply()
        }
    }
}