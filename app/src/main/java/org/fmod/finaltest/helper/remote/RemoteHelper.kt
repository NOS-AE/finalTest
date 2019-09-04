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
import org.fmod.finaltest.bean.remote.Code
import org.fmod.finaltest.bean.remote.LoginCode
import org.fmod.finaltest.bean.remote.RegisterInfo
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.helper.remote.api.ServiceProvider

class RemoteHelper {

    companion object {

        fun isNetWorkConnected(): Boolean {

            val manager = MyApp.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }

        /* QQ Field */

        private val remoteQQ = RemoteQQ()

        fun init(context: Context) {
            remoteQQ.init(context)
        }

        fun loginQQ(activity : Activity, scope: String, uiListener: IUiListener) {
            remoteQQ.login(activity, scope, uiListener)
        }

        fun getQQUserInfo(any: Any?, context: Context): Observable<UserInfo> {
            return remoteQQ.getQQUserInfo(any, context)
        }

        fun logoutQQ(context: Context) {
            remoteQQ.logoutQQ(context)
        }

        //Backend for QQ
        fun loginQQ(openId: String): Observable<LoginCode> {
            return ServiceProvider.qqLoginService().login(openId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    MyApp.token = it.token
                }
        }

        fun uploadQQInfo(info: Map<String, String>): Observable<Code> {
            return ServiceProvider.qqLoginService().uploadInfo(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /* Register Field */

        fun sendCode(email: String): Observable<Code> {
            return ServiceProvider.registerService().sendCode(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        fun register(info: Map<String, String>): Observable<RegisterInfo> {
            return ServiceProvider.registerService().register(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        fun login(email: String, password: String): Observable<LoginCode> {
            return ServiceProvider.loginService().login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        }

        fun changePassword(token: String, old: String, new: String): Observable<Code> {
            return ServiceProvider.mineService().changePassword(token, old, new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        fun getUserInfo(token: String): Observable<UserInfo> {
            return ServiceProvider.mineService().getInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }

        /*fun logout(): Observable<Code> {
            return ServiceProvider.loginService().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }*/

    }

}