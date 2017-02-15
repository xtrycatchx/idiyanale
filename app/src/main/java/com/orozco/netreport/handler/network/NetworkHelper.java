package com.orozco.netreport.handler.network;

import android.content.Context;
import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.Connectivity;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.android.gms.location.LocationRequest;
import com.orozco.netreport.model.Data;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.functions.Func2;

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

        return Observable.zip(o1, o2, new Func2<Connectivity, Location, Data>() {
            @Override
            public Data call(Connectivity connectivity, Location location) {
                Data data = new Data(connectivity, location);
                return data;
            }
        });

                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                //.subscribe(new Action1<X>() {
                //    @Override
                //    public void call(X x) {

                //        Toast.makeText(context, "Yehey " + new Gson().toJson(x), Toast.LENGTH_SHORT).show();
                //    }
                //});





        //return Observable.just(mConnectionClass.toString());
    }
}
