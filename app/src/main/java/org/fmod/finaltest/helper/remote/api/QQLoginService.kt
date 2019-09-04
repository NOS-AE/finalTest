package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.Code
import org.fmod.finaltest.bean.remote.LoginCode
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.util.toplevel.pathQQLogin
import org.fmod.finaltest.util.toplevel.pathQQUploadInfo
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface QQLoginService {

    @GET(pathQQLogin)
    fun login(@Query("openid") openId: String): Observable<LoginCode>

    @GET(pathQQUploadInfo)
    fun uploadInfo(@QueryMap info: Map<String, String>): Observable<Code>
}