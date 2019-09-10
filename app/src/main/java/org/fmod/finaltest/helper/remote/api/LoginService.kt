package org.fmod.finaltest.helper.remote.api


import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.BaseRes
import org.fmod.finaltest.bean.remote.Login
import org.fmod.finaltest.util.toplevel.pathLogin
import retrofit2.http.GET
import retrofit2.http.Query


interface LoginService {

    @GET(pathLogin)
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Observable<BaseRes<Login>>


}