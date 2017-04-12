package com.orozco.netreport.flux.model;

import android.content.Context;
import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.model.Device;
import com.orozco.netreport.model.Sources;
import com.orozco.netreport.post.api.RestAPI;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author A-Ar Andrew Concepcion
 */
public class DataCollectionModel {
    private final RestAPI mRestApi;
    private final Context mContext;
    private final Sources mSources;
    Data currentData;

    public DataCollectionModel(Context context, RestAPI restApi, Sources sources) {
        mContext = context;
        mRestApi = restApi;
        mSources = sources;
    }

    public Observable<Data> executeNetworkTest() throws SecurityException {
        Observable<Connectivity> connectivityObservable = ReactiveNetwork.observeNetworkConnectivity(mContext).first();
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        String networkOperator = mSources.networkOperator();
        Device device = mSources.device();
        String IMEI = mSources.imei();
        String signal = mSources.signal();
        String version = mSources.version();
        currentData = Data.newBuilder()
                .withOperator(networkOperator)
                .withDevice(device)
                .withImei(IMEI)
                .withSignal(signal)
                .withVersion(version)
                .build();

        Observable<Location> locationObservable = locationProvider.getUpdatedLocation(request).first();
        Observable<String> bandwidthObservable = mSources.bandwidth().subscribeOn(Schedulers.io());
        return bandwidthObservable
                .doOnNext(bandwidth -> currentData = Data.newBuilder(currentData).withBandwidth(bandwidth).build())
                .flatMap(aIgnore -> connectivityObservable)
                .doOnNext(connectivity -> currentData = Data.newBuilder(currentData).withConnectivity(connectivity).build())
                .flatMap(aIgnore -> locationObservable)
                .map(location -> currentData = Data.newBuilder(currentData).withLocation(location).build());
    }

    public Observable<Void> sendData(Data data) {
        return mRestApi.record(new Gson().toJson(data));
    }
}
