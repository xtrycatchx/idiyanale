package com.orozco.netreport.inject

import com.orozco.netreport.flux.Dispatcher
import com.orozco.netreport.flux.Utils
import com.orozco.netreport.flux.action.DataCollectionActionCreator
import com.orozco.netreport.flux.action.UserActionCreator
import com.orozco.netreport.flux.model.DataCollectionModel
import com.orozco.netreport.flux.model.UserModel

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

    @Provides
    @PerApplication
    fun providesUserActionCreator(dispatcher: Dispatcher,
                                            model: UserModel,
                                            utils: Utils): UserActionCreator {
        return UserActionCreator(dispatcher, utils, model)
    }
}
