package com.orozco.netreport.post.api;

import com.orozco.netreport.model.Data;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public interface RestAPI {

    String BASE_URL = "http://localhost:3000";

    @POST("record")
    Observable<Data> record(@Body Data data);

}
