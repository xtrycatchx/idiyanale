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
import rx.Single;
import rx.functions.Func6;
import rx.functions.Func7;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class NetworkHelper {


    @Inject
    public NetworkHelper() {
    }

    public Single<Data> executeNetworkTest(final Context context) throws SecurityException {


        Single<Connectivity> o1 = ReactiveNetwork.observeNetworkConnectivity(context).first().toSingle();

        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(100);


        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);

        Single<Location> o2 = locationProvider.getUpdatedLocation(request).first().toSingle();


        Single<String> o3 = Sources.networkOperator(context);
        Single<Device> o4 = Sources.device();
        Single<String> o5 = Sources.imei(context);
        Single<String> o6 = Sources.signal(context);
        Single<String> o7 = Sources.bandwidth();


        return Single.zip(o1, o2, o3, o4, o5, o6, o7, new Func7<Connectivity, Location, String, Device, String, String, String, Data>() {

            public Data call(Connectivity connectivity, Location location, String operator, Device device, String imei, String signal, String bandwidth) {
                Data data = new Data(connectivity, location, operator, device, imei, signal, bandwidth);
                return data;
            }
        });


    }
}
