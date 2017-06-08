package com.orozco.netreport.inject

import android.content.Context
import com.orozco.netreport.flux.model.DataCollectionModel
import com.orozco.netreport.model.Sources
import com.orozco.netreport.post.api.RestAPI
import dagger.Module
import dagger.Provides

@Module
internal class ModelModule {

    @PerApplication
    @Provides
    fun providesDataCollectionModel(context: Context, restApi: RestAPI, sources: Sources): DataCollectionModel {
        return DataCollectionModel(context, restApi, sources)
    }
}
