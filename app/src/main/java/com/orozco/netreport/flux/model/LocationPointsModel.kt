package com.orozco.netreport.flux.model

import com.orozco.netreport.model.LocationPoint
import com.orozco.netreport.post.api.RestAPI
import rx.Observable

/**
 * @author A-Ar Andrew Concepcion
 */
class LocationPointsModel(private val mRestApi: RestAPI) {
    fun getLocationPoints(): Observable<List<LocationPoint>> {
        return mRestApi.getLocationPoints()
    }
}
