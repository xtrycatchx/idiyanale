package com.orozco.netreport.post.api

import com.orozco.netreport.model.Data

import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

interface RestAPI {

    @POST("record")
    fun record(@Body data: Data): Observable<Data>

    companion object {
        val BASE_URL = "https://bass.bnshosting.net/api/v2/"
    }

}
