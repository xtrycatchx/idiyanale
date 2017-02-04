package com.orozco.netreport.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class Data {

    @SerializedName("imei")
    @Expose
    private String imei;

    @SerializedName("carrier")
    @Expose
    private String carrier;

    @SerializedName("phone")
    @Expose
    private String phoneModel;

    @SerializedName("os")
    @Expose
    private String oSversion;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;

    @SerializedName("signal")
    @Expose
    private String signal;

    @SerializedName("connection")
    @Expose
    private String connection;

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

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }
}
