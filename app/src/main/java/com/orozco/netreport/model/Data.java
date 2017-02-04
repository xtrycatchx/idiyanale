package com.orozco.netreport.model;

import java.util.Date;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class Data {

    private String imei;
    private String carrier;
    private String phoneModel;
    private String oSversion;
    private Date date;
    private Coordinates coordinates;
    private Signal signal;
    private int bandwithSpeed;
    private ConnectionType conenctionType;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getoSversion() {
        return oSversion;
    }

    public void setoSversion(String oSversion) {
        this.oSversion = oSversion;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public int getBandwithSpeed() {
        return bandwithSpeed;
    }

    public void setBandwithSpeed(int bandwithSpeed) {
        this.bandwithSpeed = bandwithSpeed;
    }

    public ConnectionType getConenctionType() {
        return conenctionType;
    }

    public void setConenctionType(ConnectionType conenctionType) {
        this.conenctionType = conenctionType;
    }
}
