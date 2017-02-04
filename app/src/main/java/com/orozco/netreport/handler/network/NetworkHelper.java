package com.orozco.netreport.handler.network;

import java.util.Random;

import javax.inject.Inject;

import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class NetworkHelper {
    @Inject
    public NetworkHelper() {
    }

    public Observable<String> executeNetworkTest() {

        String[] list = {"POOR", "OK", "GREAT", "AWESOME", "UNKNOWN"};
        Random r = new Random();

        return Observable.just(list[r.nextInt(list.length)]);
    }

    public Observable<String> retrieveNetworkCarrier() {

        String[] list = {"SMART", "GLOBE"};
        Random r = new Random();

        return Observable.just(list[r.nextInt(list.length)]);
    }
}
