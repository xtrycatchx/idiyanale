package com.orozco.netreport.model;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class DataTemp {
    private DataEnum flag;
    private String dataRaw;

    public DataTemp(DataEnum flag, String dataRaw) {
        this.flag = flag;
        this.dataRaw = dataRaw;
    }

    public DataEnum getFlag() {
        return flag;
    }

    public void setFlag(DataEnum flag) {
        this.flag = flag;
    }

    public String getDataRaw() {
        return dataRaw;
    }

    public void setDataRaw(String dataRaw) {
        this.dataRaw = dataRaw;
    }
}
