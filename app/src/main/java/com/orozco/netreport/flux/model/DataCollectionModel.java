package com.orozco.netreport.flux.model;

import android.content.Context;
import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.android.gms.location.LocationRequest;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.model.Device;
import com.orozco.netreport.model.Sources;
import com.orozco.netreport.post.api.RestAPI;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

/**
 * @author A-Ar Andrew Concepcion
 */
public class DataCollectionModel {
    private final RestAPI mRestApi;
    private final Context mContext;
    private final Sources mSources;

    public DataCollectionModel(Context context, RestAPI restApi, Sources sources) {
        mContext = context;
        mRestApi = restApi;
        mSources = sources;
    }

    public Single<Data> executeNetworkTest() throws SecurityException {
        Single<Connectivity> connectivitySingle = ReactiveNetwork.observeNetworkConnectivity(mContext).first().toSingle();
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(mContext);

        String networkOperator = mSources.networkOperator();
        Device device = mSources.device();
        String IMEI = mSources.imei();
        String signal = mSources.signal();

        Data currentData = Data.newBuilder()
                .withOperator(networkOperator)
                .withDevice(device)
                .withImei(IMEI)
                .withSignal(signal)
                .build();

        Single<Location> locationSingle = locationProvider.getUpdatedLocation(request).first().toSingle();
        Single<String> bandwidthSingle = mSources.bandwidth().subscribeOn(Schedulers.io());

        return Single.zip(connectivitySingle, locationSingle, bandwidthSingle, (connectivity, location, bandwidth) ->
                Data.newBuilder(currentData)
                        .withConnectivity(connectivity)
                        .withLocation(location)
                        .withBandwidth(bandwidth)
                        .build())
                .onErrorReturn(throwable -> currentData); // do not be dependent on location and bandwidth, if it causes an error, still send some data
    }

    public Observable<Data> sendData(Data data) {
        return mRestApi.record(data);
    }
}
