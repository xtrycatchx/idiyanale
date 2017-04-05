package com.orozco.netreport.inject;

import android.content.Context;

import com.orozco.netreport.flux.model.DataCollectionModel;
import com.orozco.netreport.inject.PerApplication;
import com.orozco.netreport.model.Sources;
import com.orozco.netreport.post.api.RestAPI;

import dagger.Module;
import dagger.Provides;

@Module
class ModelModule {

    @PerApplication
    @Provides
    public DataCollectionModel providesDataCollectionModel(Context context, RestAPI restApi, Sources sources) {
        return new DataCollectionModel(context, restApi, sources);
    }
}
