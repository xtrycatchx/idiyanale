package com.orozco.netreport.inject

import com.orozco.netreport.flux.Dispatcher
import com.orozco.netreport.flux.Utils
import com.orozco.netreport.flux.action.DataCollectionActionCreator
import com.orozco.netreport.flux.model.DataCollectionModel

import dagger.Module
import dagger.Provides

/**
 * @author A-Ar Andrew Concepcion
 */
@Module
internal class ActionCreatorModule {
    @Provides
    @PerApplication
    fun providesDataCollectionActionCreator(dispatcher: Dispatcher,
                                            model: DataCollectionModel,
                                            utils: Utils): DataCollectionActionCreator {
        return DataCollectionActionCreator(dispatcher, utils, model)
    }
}
