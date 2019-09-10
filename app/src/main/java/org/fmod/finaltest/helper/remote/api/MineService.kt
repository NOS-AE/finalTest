package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.BaseRes
import org.fmod.finaltest.bean.remote.State
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.util.toplevel.pathChangeEmail
import org.fmod.finaltest.util.toplevel.pathChangePw
import org.fmod.finaltest.util.toplevel.pathRename
import org.fmod.finaltest.util.toplevel.pathUserInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface MineService {
    @GET(pathUserInfo)
    fun getInfo(@Query("token") token: String): Observable<BaseRes<UserInfo>>

    @GET(pathRename)
    fun changeName(@Query("token") token: String, @Query("username") newName: String): Observable<State>

    @GET(pathChangeEmail)
    fun sendCode(@Query("token") token: String, @Query("email") email: String): Observable<State>

    @GET(pathChangeEmail)
    fun changeEmail(@Query("token") token: String, @Query("email") email: String, @Query("code") code: String): Observable<State>

    @GET(pathChangePw)
    fun changePassword(@Query("token") token: String,@Query("password") old: String, @Query("newpwd") new: String): Observable<State>
}