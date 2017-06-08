package com.orozco.netreport.flux.store

import com.orozco.netreport.flux.Action
import com.orozco.netreport.flux.AppError
import com.orozco.netreport.flux.Store
import com.orozco.netreport.flux.action.DataCollectionActionCreator
import com.orozco.netreport.model.Data
import rx.Observable

/**
 * @author A-Ar Andrew Concepcion
 */
class DataCollectionStore : Store<DataCollectionStore>() {
    var data: Data? = null
        private set
    var action: String? = null
        private set
    var error: AppError? = null
        private set

    fun observableWithFilter(filter: String): Observable<DataCollectionStore> {
        return observable().filter { store -> filter == store.action }
    }

    private fun updateState() {
        error = null
    }

    private fun updateData(action: Action) {
        if (action.data() is Data) {
            data = action.data() as Data?
        }
    }

    private fun updateError(action: Action) {
        if (action.data() != null && action.data() is AppError) {
            error = action.data() as AppError?
        }
    }

    private fun updateAction(action: Action) {
        this.action = action.type()
    }

    override fun onReceiveAction(action: Action) {
        when (action.type()) {
            DataCollectionActionCreator.ACTION_COLLECT_DATA_S,
            DataCollectionActionCreator.ACTION_COLLECT_DATA_F,
            DataCollectionActionCreator.ACTION_SEND_DATA_S,
            DataCollectionActionCreator.ACTION_SEND_DATA_F -> {
                updateState()
                updateData(action)
                updateError(action)
                updateAction(action)
                notifyStoreChanged(this)
            }
        }
    }
}
