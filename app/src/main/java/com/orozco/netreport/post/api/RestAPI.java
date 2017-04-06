package com.orozco.netreport.post.api;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public interface RestAPI {

    String BASE_URL = "https://bass.bnshosting.net/api";

    @POST("/record")
    Observable<Void> record(@Body String data);

}
