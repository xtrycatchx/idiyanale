package com.orozco.netreport.inject

import android.content.Context
import com.orozco.netreport.core.Database
import com.orozco.netreport.flux.model.DataCollectionModel
import com.orozco.netreport.flux.model.LocationPointsModel
import com.orozco.netreport.model.Sources
import com.orozco.netreport.post.api.RestAPI
import dagger.Module
import dagger.Provides

@Module
internal class ModelModule {

    @PerApplication
    @Provides
    fun providesDataCollectionModel(context: Context, restApi: RestAPI, sources: Sources, database: Database): DataCollectionModel {
        return DataCollectionModel(context, restApi, sources, database)
    }

    @PerApplication
    @Provides
    fun providesLocationPointsModel(restApi: RestAPI): LocationPointsModel {
        return LocationPointsModel(restApi)
    }
}
