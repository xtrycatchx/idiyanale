package com.orozco.netreport.handler;

import android.content.Context;

import com.orozco.netreport.common.Schedulers;
import com.orozco.netreport.handler.network.NetworkHelper;
import com.orozco.netreport.model.Data;

import javax.inject.Inject;

import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class StartTest {
    private final Schedulers schedulers;
    private final NetworkHelper helper;

    @Inject
    public StartTest(Schedulers schedulers, NetworkHelper helper) {
        this.schedulers = schedulers;
        this.helper = helper;
    }

    public Observable<Data> execute(Context context) {
        return helper.executeNetworkTest(context).compose(schedulers.<Data>apply());
    }
}
