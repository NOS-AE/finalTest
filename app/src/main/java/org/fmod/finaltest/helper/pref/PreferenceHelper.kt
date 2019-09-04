package org.fmod.finaltest.helper.pref

import android.content.Context
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.util.toplevel.*

class PreferenceHelper {
    companion object{

        const val wayNoLogin = -1
        const val wayEmail = 0
        const val wayQQ = 1

        private var sPreference = MyApp.appContext.getSharedPreferences("data",Context.MODE_PRIVATE)

        private val loginState = sPreference.getInt(LOGIN_WAY_PARAM, wayNoLogin)

        fun isFirstApp(): Boolean {
            return sPreference.getBoolean(FIRST_APP_PARAM,true)
        }

        fun setFirstApp(first: Boolean) {
            sPreference
                .edit()
                .putBoolean(AUTO_LOGIN_PARAM, true)
                .putInt(LOGIN_WAY_PARAM, wayNoLogin)
                .putBoolean(FIRST_APP_PARAM, first)
                .apply()
        }

        fun setAutoLogin(autoLogin: Boolean) {
            val editor = sPreference.edit()

            if(!autoLogin)
                editor.putInt(LOGIN_WAY_PARAM, wayNoLogin)
            else
                editor.putInt(LOGIN_WAY_PARAM, loginState)

            editor
                .putBoolean(AUTO_LOGIN_PARAM, autoLogin)
                .apply()
        }

        fun isLoginOnStart(): Boolean {
            if(!sPreference.getBoolean(AUTO_LOGIN_PARAM, true))
                return false
            return sPreference.getInt(LOGIN_WAY_PARAM, wayNoLogin) != wayNoLogin
        }

        fun saveMailLogin(email: String, password: String) {
            sPreference
                .edit()
                .putString(EMAIL_PARAM, email)
                .putString(EMAIL_PW_PARAM, password)
                .apply()
        }

        fun saveMail(email: String) {
            sPreference
                .edit()
                .putString(EMAIL_PARAM, email)
                .apply()
        }

        fun saveMailPw(password: String) {
            sPreference
                .edit()
                .putString(EMAIL_PW_PARAM, password)
                .apply()
        }

        fun saveQQLogin(openId: String) {
            sPreference
                .edit()
                .putString(QQ_OPENID_PARAM, openId)
                .apply()
        }

        fun setLoginWay(way: Int) {
            sPreference
                .edit()
                .putInt(LOGIN_WAY_PARAM, way)
                .apply()
        }

        fun getLoginWay() = sPreference.getInt(LOGIN_WAY_PARAM, wayNoLogin)

        fun getEmail(): String? = sPreference.getString(EMAIL_PARAM, "")

        fun getEmailPassword(): String? = sPreference.getString(EMAIL_PW_PARAM, "")

        fun getQQOpenId(): String? = sPreference.getString(QQ_OPENID_PARAM, "")
    }


}