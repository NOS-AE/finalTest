package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.BaseRes
import org.fmod.finaltest.bean.remote.Login
import org.fmod.finaltest.bean.remote.State
import org.fmod.finaltest.util.toplevel.pathQQLogin
import org.fmod.finaltest.util.toplevel.pathQQUploadInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface QQLoginService {

    @GET(pathQQLogin)
    fun login(@Query("openid") openId: String): Observable<BaseRes<Login>>

    @GET(pathQQUploadInfo)
    fun uploadInfo(
        @Query("token") token: String,
        @Query("username") name: String,
        @Query("avatar") avatar: String
    ): Observable<State>
}