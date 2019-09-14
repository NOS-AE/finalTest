package org.fmod.finaltest.helper.pref.setting

object SettingPrefImpl: ISettingPref {
    override var firstLaunch: Boolean by SettingPrefHelper(true)
    override var autoLogin: Boolean by SettingPrefHelper(true)
    override var informDaily: Boolean by SettingPrefHelper(true)
    override fun clearSettingPref() {
        SettingPrefHelper.clear()
    }
}