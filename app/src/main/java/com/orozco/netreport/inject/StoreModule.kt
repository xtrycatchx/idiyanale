package com.orozco.netreport.inject

import com.orozco.netreport.flux.Dispatcher
import com.orozco.netreport.flux.store.DataCollectionStore
import dagger.Module
import dagger.Provides
import java.util.*

/**
 * Dagger module to provide flux components. This class will contain the Dispatcher,
 * ActionCreators, and Stores.

 * @author Gian Darren Aquino
 */
@Module
internal class StoreModule {

    @PerApplication
    @Provides
    fun providesDispatcher(dataCollectionStore: DataCollectionStore): Dispatcher {
        return Dispatcher(Arrays.asList(dataCollectionStore))
    }

    @PerApplication
    @Provides
    fun providesDataCollectionStore(): DataCollectionStore {
        return DataCollectionStore()
    }
}
