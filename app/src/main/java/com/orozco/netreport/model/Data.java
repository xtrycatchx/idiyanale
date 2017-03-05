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
    private String bandwidth;

    public Data() {

    }

    public Data(Connectivity connectivity, Location location, String operator, Device device, String imei, String signal, String bandwidth) {
        this.connectivity = connectivity;
        this.location = location;
        this.operator = operator;
        this.device = device;
        this.imei = imei;
        this.signal = signal;
        this.bandwidth = bandwidth;
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

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }
}
