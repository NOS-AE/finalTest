package org.fmod.finaltest.helper.pref.user

/**
 * 键值对的具体实现在UserPrefImpl中
 */
interface IUserPref {
    var email: String//用户邮箱
    var password: String//邮箱对应的密码
    var loginWay: Int//登录方式
    var qqId: String//qq用户id
    var wechatId: String//微信用户id
    var weiboId: String//微博用户id
    fun clearUserPref()
}

object LoginWay {
    const val NO_LOGIN = 0//没有登录，或已退出登录
    const val EMAIL = 1//邮箱
    const val QQ = 2//QQ
    const val WECHAT = 3//微信
    const val WEIBO = 4//微博
}