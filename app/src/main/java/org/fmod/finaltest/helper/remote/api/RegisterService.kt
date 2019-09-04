package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.Code
import org.fmod.finaltest.bean.remote.RegisterInfo
import org.fmod.finaltest.util.toplevel.pathRegister
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RegisterService {

    @GET(pathRegister)
    fun sendCode(@Query("email") email: String): Observable<Code>

    @GET(pathRegister)
    fun register(@QueryMap info: Map<String, String>): Observable<RegisterInfo>

}