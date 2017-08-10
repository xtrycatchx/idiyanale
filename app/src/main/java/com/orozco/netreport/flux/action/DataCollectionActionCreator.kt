package com.orozco.netreport.flux.action

import android.support.annotation.StringDef
import com.orozco.netreport.flux.Action
import com.orozco.netreport.flux.Dispatcher
import com.orozco.netreport.flux.Utils
import com.orozco.netreport.flux.model.DataCollectionModel
import com.orozco.netreport.model.Data

/**
 * @author A-Ar Andrew Concepcion
 */
class DataCollectionActionCreator(private val mDispatcher: Dispatcher, private val mUtils: Utils, private val mModel: DataCollectionModel) {

    companion object {
        const val ACTION_COLLECT_DATA_S = "ACTION_COLLECT_DATA_S"
        const val ACTION_COLLECT_DATA_F = "ACTION_COLLECT_DATA_F"
        const val ACTION_SEND_DATA_S = "ACTION_SEND_DATA_S"
        const val ACTION_SEND_DATA_F = "ACTION_SEND_DATA_F"
    }

    @StringDef(value = *arrayOf(ACTION_COLLECT_DATA_S, ACTION_COLLECT_DATA_F, ACTION_SEND_DATA_S, ACTION_SEND_DATA_F))
    @Retention(AnnotationRetention.SOURCE)
    annotation class DataCollectionAction

    fun collectData() {
        mModel.executeNetworkTest()
                .subscribe({ data -> mDispatcher.dispatch(Action.create(ACTION_COLLECT_DATA_S, data)) }) { throwable -> mDispatcher.dispatch(Action.create(ACTION_COLLECT_DATA_F, mUtils.getError(throwable))) }
    }

    fun sendData(data: Data) {
        mModel.sendData(data)
                .subscribe({ mVoid -> mDispatcher.dispatch(Action.create(ACTION_SEND_DATA_S, data)) }) { throwable ->
                    mDispatcher.dispatch(Action.create(ACTION_SEND_DATA_F, mUtils.getError(throwable))) }
    }
}
