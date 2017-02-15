package com.orozco.netreport.model;

import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.Connectivity;


/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class Data {

    private Connectivity connectivity;
    private Location location;

    public Data() {

    }

    public Data(Connectivity connectivity, Location location) {
        this.connectivity = connectivity;
        this.location = location;
    }

    public Connectivity getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(Connectivity connectivity) {
        this.connectivity = connectivity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
