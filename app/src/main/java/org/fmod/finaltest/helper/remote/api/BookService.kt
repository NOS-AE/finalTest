package org.fmod.finaltest.helper.remote.api

import io.reactivex.Observable
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.remote.TimeCode
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface BookService {

    @GET("main/itemadd.php")
    fun syncDealItem(@QueryMap params: Map<String, String>): Observable<TimeCode>

}