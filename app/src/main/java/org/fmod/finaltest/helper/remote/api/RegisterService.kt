package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.State
import org.fmod.finaltest.util.toplevel.pathRegister
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RegisterService {

    @GET(pathRegister)
    fun sendCode(@Query("email") email: String): Observable<State>

    @GET(pathRegister)
    fun register(
        @Query("email") email: String,
        @Query("code") code: String,
        @Query("password") password: String
    ): Observable<State>

}