package com.orozco.netreport.inject;

import com.orozco.netreport.flux.Dispatcher;
import com.orozco.netreport.flux.store.DataCollectionStore;
import com.orozco.netreport.inject.PerApplication;

import java.util.Arrays;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module to provide flux components. This class will contain the Dispatcher,
 * ActionCreators, and Stores.

 * @author Gian Darren Aquino
 */
@Module
class StoreModule {

    @PerApplication
    @Provides
    public Dispatcher providesDispatcher(DataCollectionStore dataCollectionStore) {
        return new Dispatcher(Arrays.asList(dataCollectionStore));
    }

    @PerApplication
    @Provides
    public DataCollectionStore providesDataCollectionStore() {
        return new DataCollectionStore();
    }
}
