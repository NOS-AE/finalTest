package org.fmod.finaltest.helper.remote

import android.app.Activity
import android.content.Context
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.fmod.finaltest.base.abstracts.UiListener
import org.fmod.finaltest.bean.remote.QUserInfo
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.helper.json.JsonHelper
import org.fmod.finaltest.util.toplevel.toast
import org.json.JSONException
import org.json.JSONObject

class RemoteQQ {

    companion object {

        private const val tencentAppId = "1109728543"

        private lateinit var sTencent: Tencent

        fun getOpenId() = sTencent.openId
    }



    fun init(context: Context) {
        //通过Tencent实例访问OpenAPI
        sTencent = Tencent.createInstance(tencentAppId, context)
    }

    fun login(activity : Activity, scope: String, uiListener: IUiListener) {

        if(!sTencent.isQQInstalled(activity))
            toast("未安装QQ")
        else if(!sTencent.isSessionValid)
            sTencent.login(activity, scope, uiListener)

    }

    /**
     * @param any 用户唯一标识符，token等信息
     *
     * @param context 上下文
     *
     */
    fun getQQUserInfo(any: Any?, context: Context): Observable<QUserInfo> {

        return Observable.create<QUserInfo> { emitter ->
            val jsonObject = any as JSONObject
            try {

                val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
                val accessToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
                val expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN)

                sTencent.openId = openId
                sTencent.setAccessToken(accessToken, expires)

            }catch (e: JSONException){
                emitter.onError(e)
            }

            com.tencent.connect.UserInfo(context, sTencent.qqToken)
                .getUserInfo(object : UiListener(){
                override fun onComplete(p0: Any?) {
                    super.onComplete(p0)
                    val json = (p0 as JSONObject).toString()
                    val info = JsonHelper.convert<QUserInfo>(json)
                    emitter.onNext(info)
                }
            })
        }
    }


    fun logoutQQ(context: Context) {
        if(sTencent.isSessionValid) {
            sTencent.logout(context)
        }
    }
}