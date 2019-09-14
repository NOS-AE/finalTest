package org.fmod.finaltest.helper.remote

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.tencent.tauth.IUiListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.bean.remote.*
import org.fmod.finaltest.helper.json.JsonHelper
import org.fmod.finaltest.helper.remote.api.ServiceProvider
import org.fmod.finaltest.util.toplevel.networkLog

class RemoteHelper {

    companion object {

        fun isNetWorkConnected(): Boolean {

            val manager = MyApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }

        /* QQ Field */

        private val remoteQQ = RemoteQQ()
        /**
         * 初始化QQ
         *
         * @param context
         */
        fun init(context: Context) {
            remoteQQ.init(context)
        }

        /**
         * 打开QQ登录界面
         *
         * @param activity 建立在activity之上打开
         * @param scope 要访问用户的信息域
         * @param uiListener 登录回调
         */
        fun loginQQ(activity : Activity, scope: String, uiListener: IUiListener) {
            remoteQQ.login(activity, scope, uiListener)
        }

        /**
         * 获取QQ用户信息
         *
         * @param any QQ登录后的信息
         * @param context
         */
        fun getQQUserInfo(any: Any?, context: Context): Observable<QUserInfo> {
            return remoteQQ.getQQUserInfo(any, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 退出QQ登录
         *
         * @param context
         */
        fun logoutQQ(context: Context) {
            remoteQQ.logoutQQ(context)
        }

        /* Backend for QQ */
        /**
         * QQ用户登录
         */
        fun loginQQ(openId: String): Observable<BaseRes<Login>> {
            return ServiceProvider.qqLoginService().login(openId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    JsonHelper.toJson(it)
                }
        }

        /**
         * 上传QQ用户的信息到后端
         */
        fun uploadQQInfo(name: String, avatar: String): Observable<State> {
            return ServiceProvider.qqLoginService().uploadInfo(MyApp.token, name, avatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /* Register Field */
        /**
         * 注册发送验证码
         *
         * @param email
         */
        fun sendCode(email: String): Observable<State> {
            return ServiceProvider.registerService().sendCode(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 用户注册
         *
         * @param
         */
        fun register(email: String, code: String, password: String): Observable<State> {
            return ServiceProvider.registerService().register(email, code, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 用户登录
         */
        fun login(email: String, password: String): Observable<BaseRes<Login>> {
            return ServiceProvider.loginService().login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    networkLog("Login: ${JsonHelper.toJson(it)}")
                }
        }

        /**
         * 用户修改密码
         */
        fun changePassword(old: String, new: String): Observable<State> {
            return ServiceProvider.mineService().changePassword(MyApp.token, old, new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 修改用户名
         */
        fun changeName(name: String): Observable<State> {
            return ServiceProvider.mineService().changeName(MyApp.token, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 修改邮箱前发送验证码
         */
        fun changeEmailSendCode(email: String): Observable<State> {
            return ServiceProvider.mineService().sendCode(MyApp.token, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /**
         * 修改邮箱
         */
        fun changeEmail(email: String, code: String): Observable<State> {
            return ServiceProvider.mineService().changeEmail(MyApp.token, email, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        fun getUserInfo(): Observable<BaseRes<UserInfo>> {
            return ServiceProvider.mineService().getInfo(MyApp.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

    }

}