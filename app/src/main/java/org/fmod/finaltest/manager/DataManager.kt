package org.fmod.finaltest.manager

import org.fmod.finaltest.helper.pref.setting.ISettingPref
import org.fmod.finaltest.helper.pref.setting.SettingPrefImpl
import org.fmod.finaltest.helper.pref.user.IUserPref
import org.fmod.finaltest.helper.pref.user.UserPrefImpl

/**
 * 管理本地数据
 */
object DataManager: IUserPref by UserPrefImpl, ISettingPref by SettingPrefImpl