package org.fmod.finaltest.helper.pref.user

/**
 * 键值对的存取方式委托于UserPrefHelper类
 */
object UserPrefImpl: IUserPref {
    override var email: String by UserPrefHelper("")
    override var password: String by UserPrefHelper("")
    override var loginWay: Int by UserPrefHelper(LoginWay.NO_LOGIN)
    override var qqId: String by UserPrefHelper("")
    override var wechatId: String by UserPrefHelper("")
    override var weiboId: String by UserPrefHelper("")
    override fun clearUserPref() {
        UserPrefHelper.clear()
    }
}