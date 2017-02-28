package com.orozco.netreport.model;

/**
 * Created by sydney on 16/2/17.
 */

public class Device {
    private String manufacturer;
    private String model;
    private String release;
    private String name;

    public Device(String manufacturer, String model, String release, String name) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.release = release;
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
