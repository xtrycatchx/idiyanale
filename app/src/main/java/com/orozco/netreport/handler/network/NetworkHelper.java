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
import rx.Single;
import rx.functions.Func7;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class NetworkHelper {

    @Inject
    public NetworkHelper() {
    }
}
