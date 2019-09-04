package org.fmod.finaltest.helper.remote.api


import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.Code
import org.fmod.finaltest.bean.remote.LoginCode
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.util.toplevel.pathLogin
import org.fmod.finaltest.util.toplevel.pathLogout
import retrofit2.http.GET
import retrofit2.http.Query


interface LoginService {

    @GET(pathLogin)
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Observable<LoginCode>


}