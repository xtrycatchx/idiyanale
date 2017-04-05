package com.orozco.netreport.inject;

import com.orozco.netreport.flux.Dispatcher;
import com.orozco.netreport.flux.Utils;
import com.orozco.netreport.flux.action.DataCollectionActionCreator;
import com.orozco.netreport.flux.model.DataCollectionModel;

import dagger.Module;
import dagger.Provides;

/**
 * @author A-Ar Andrew Concepcion
 */
@Module
class ActionCreatorModule {
    @Provides
    @PerApplication
    DataCollectionActionCreator providesDataCollectionActionCreator(Dispatcher dispatcher,
                                                                    DataCollectionModel model,
                                                                    Utils utils) {
        return new DataCollectionActionCreator(dispatcher, utils, model);
    }
}
