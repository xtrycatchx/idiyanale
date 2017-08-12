package com.orozco.netreport.post.api

import com.orozco.netreport.model.Data
import com.orozco.netreport.model.LocationPoint
import com.orozco.netreport.model.RecordResponse

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import rx.Observable

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

interface RestAPI {

    @POST("scanresults")
    fun record(@Body data: Data): Observable<RecordResponse>

    @GET("locationpoints")
    fun getLocationPoints(): Observable<List<LocationPoint>>

    companion object {
        val BASE_URL = "https://bass.bnshosting.net/v2/api/"
    }

}
