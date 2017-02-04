package com.orozco.netreport.handler;

import com.orozco.netreport.common.Schedulers;
import com.orozco.netreport.handler.network.NetworkHelper;

import javax.inject.Inject;

import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class RetrieveLocation {
    private final Schedulers schedulers;
    private final NetworkHelper helper;

    @Inject
    public RetrieveLocation(Schedulers schedulers, NetworkHelper helper) {
        this.schedulers = schedulers;
        this.helper = helper;
    }

    public Observable<String> execute() {
        return helper.executeNetworkTest().compose(schedulers.<String>apply());
    }
}
