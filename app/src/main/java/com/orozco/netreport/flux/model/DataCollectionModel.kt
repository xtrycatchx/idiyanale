package com.orozco.netreport.flux.model

import android.content.Context

import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
import com.google.android.gms.location.LocationRequest
import com.orozco.netreport.model.Data
import com.orozco.netreport.model.Sources
import com.orozco.netreport.post.api.RestAPI

import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import rx.Observable
import rx.schedulers.Schedulers

/**
 * @author A-Ar Andrew Concepcion
 */
class DataCollectionModel(private val mContext: Context, private val mRestApi: RestAPI, private val mSources: Sources) {
    @Throws(SecurityException::class)
    fun executeNetworkTest(): Observable<Data> {
        val connectivityObservable = ReactiveNetwork.observeNetworkConnectivity(mContext).first()
        val request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100)
        val locationProvider = ReactiveLocationProvider(mContext)

        val networkOperator = mSources.networkOperator()
        val device = mSources.device()
        val IMEI = mSources.imei()
        val signal = mSources.signal()
        val version = mSources.version()
        var currentData = Data(
                operator = networkOperator,
                device = device,
                imei = IMEI,
                signal = signal,
                version = version)

        val locationObservable = locationProvider.getUpdatedLocation(request).first()
        val bandwidthObservable = mSources.bandwidth().subscribeOn(Schedulers.io())
        return bandwidthObservable
                .doOnNext { currentData = currentData.copy(bandwidth = it)}
                .flatMap { aIgnore -> connectivityObservable }
                .doOnNext { currentData = currentData.copy(connectivity = it) }
                .flatMap { aIgnore -> locationObservable }
                .map { currentData.copy(location = it) }
    }

    fun sendData(data: Data): Observable<Data> {
        return mRestApi.record(data)
    }
}
