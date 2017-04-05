package com.orozco.netreport.inject;

import com.orozco.netreport.post.api.RestAPI;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * YOYO HOLDINGS

 * @author A-Ar Andrew Concepcion
 * *
 * @since 14/12/2016
 */
@Module
public class RestModule {
    @Provides
    @PerApplication
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(RestAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    @Provides
    @PerApplication
    public RestAPI provideRestApi(Retrofit retrofit) {
        return retrofit.create(RestAPI.class);
    }
}