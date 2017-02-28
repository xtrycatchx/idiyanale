package com.orozco.netreport.handler.network;

import android.content.Context;
import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.android.gms.location.LocationRequest;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.model.Device;
import com.orozco.netreport.model.Sources;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Func6;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class NetworkHelper {


    @Inject
    public NetworkHelper() {
    }

    public Observable<Data> executeNetworkTest(final Context context) throws SecurityException {


        Observable<Connectivity> o1 = ReactiveNetwork.observeNetworkConnectivity(context);
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
        Observable<Location> o2 = locationProvider.getUpdatedLocation(request);


        Observable<String> o3 = Sources.networkOperator(context);
        Observable<Device> o4 = Sources.device();
        Observable<String> o5 = Sources.imei(context);
        Observable<String> o6 = Sources.signal(context);


        return Observable.zip(o1, o2, o3, o4, o5, o6, new Func6<Connectivity, Location, String, Device, String, String, Data>() {
            @Override
            public Data call(Connectivity connectivity, Location location, String operator, Device device, String imei, String signal) {
                Data data = new Data(connectivity, location, operator, device, imei, signal);
                return data;
            }
        });
    }
}
