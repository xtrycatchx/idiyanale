package com.orozco.netreport.post.api;

import com.orozco.netreport.model.Data;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public interface RestAPI {

    @POST("/record")
    Observable<ResponseBody> record(@Body Data data);
}
