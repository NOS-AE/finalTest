package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.remote.Code
import org.fmod.finaltest.bean.remote.UserInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface MineService {
    @GET("mine")
    fun getInfo(@Query("token") token: String): Observable<UserInfo>

    @GET("mine/upname.php")
    fun rename(@Query("token") token: String, @Query("username") newName: String): Observable<UserInfo>

    /*@GET("mine/upemail.php")
    fun changeMailbox(@Query(""))*/

    @GET("mine/uppwd.php")
    fun changePassword(@Query("token") token: String,@Query("password") old: String, @Query("newpwd") new: String): Observable<Code>
}