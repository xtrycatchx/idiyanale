package com.orozco.netreport.flux.action

import android.support.annotation.StringDef
import com.orozco.netreport.flux.Action
import com.orozco.netreport.flux.Dispatcher
import com.orozco.netreport.flux.Utils
import com.orozco.netreport.flux.model.LocationPointsModel

/**
 * @author A-Ar Andrew Concepcion
 */
class LocationPointsActionCreator(private val mDispatcher: Dispatcher, private val mUtils: Utils, private val mModel: LocationPointsModel) {

    companion object {
        const val ACTION_GET_LOCATION_POINTS_S = "ACTION_GET_LOCATION_POINTS_S"
        const val ACTION_GET_LOCATION_POINTS_F = "ACTION_GET_LOCATION_POINTS_F"
    }

    @StringDef(value = *arrayOf(ACTION_GET_LOCATION_POINTS_S, ACTION_GET_LOCATION_POINTS_F))
    @Retention(AnnotationRetention.SOURCE)
    annotation class LocationPointsAction

    fun getLocationPoints() {
        mModel.getLocationPoints()
                .subscribe({ mDispatcher.dispatch(Action.create(ACTION_GET_LOCATION_POINTS_S, it)) }) { throwable ->
                    mDispatcher.dispatch(Action.create(ACTION_GET_LOCATION_POINTS_F, mUtils.getError(throwable))) }
    }
}
