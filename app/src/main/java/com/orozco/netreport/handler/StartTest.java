package com.orozco.netreport.handler;

import com.orozco.netreport.common.Schedulers;
import com.orozco.netreport.handler.network.NetworkHelper;

import javax.inject.Inject;

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
}
