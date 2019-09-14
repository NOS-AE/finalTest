package org.fmod.finaltest.helper.pref.setting

interface ISettingPref {
    var firstLaunch: Boolean//第一次打开App
    var autoLogin: Boolean//自动登录
    var informDaily: Boolean//每日记账提醒
    fun clearSettingPref()
}