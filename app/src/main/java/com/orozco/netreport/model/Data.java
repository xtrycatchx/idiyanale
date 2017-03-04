package com.orozco.netreport.model;

import android.location.Location;

import com.github.pwittchen.reactivenetwork.library.Connectivity;


/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class Data {

    private Connectivity connectivity;
    private Location location;
    private String operator;
    private Device device;
    private String imei;
    private String signal;

    public Data() {

    }

    public Data(Connectivity connectivity, Location location, String operator, Device device, String imei, String signal) {
        this.connectivity = connectivity;
        this.location = location;
        this.operator = operator;
        this.device = device;
        this.imei = imei;
        this.signal = signal;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }
}
