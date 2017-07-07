package com.orozco.netreport.post.api

import com.orozco.netreport.model.Data
import com.orozco.netreport.model.Token

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

interface RestAPI {

    @POST("record")
    fun record(@Body data: Data): Observable<Data>

    @POST("oauth/token")
    fun authenticate(@QueryMap credentials: Map<String, String>): Observable<Token>

    @POST("public/register")
    fun register(@QueryMap credentials: Map<String, String>): Observable<Unit>

    companion object {
        val BASE_URL = "https://bass.bnshosting.net/api/v2/"
    }

}
