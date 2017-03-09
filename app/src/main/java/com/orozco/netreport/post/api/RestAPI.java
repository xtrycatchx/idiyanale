package com.orozco.netreport.post.api;

import com.orozco.netreport.model.Data;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public interface RestAPI {

    String BASE_URL = "REPLACE"; // format is "www.google.com"
    String API = "http://"+BASE_URL+"REPLACE"; //format is ":{port}/{apiEndpoint}/"

    @POST("record")
    Observable<String> record(@Body String data);

    class Factory {
        public static RestAPI create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(RestAPI.class);
        }
    }
}
