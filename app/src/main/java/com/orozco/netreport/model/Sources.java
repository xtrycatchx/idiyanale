package com.orozco.netreport.model;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import rx.Single;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class Sources {

    private final Context context;

    public Sources(Context context) {
        this.context = context;
    }

    public String networkOperator() {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
    }

    public Device device() {
        return new Device(Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());
    }

    public String imei() {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }


    public String signal() {
        String signalStrength = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            CellInfo cellinfo = telephonyManager.getAllCellInfo().get(0);
            int dbm = -666;
            if (cellinfo instanceof CellInfoWcdma) {
                CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma) cellinfo).getCellSignalStrength();
                dbm = cellSignalStrengthWcdma.getDbm();
                signalStrength = "WCDMA";
            } else if (cellinfo instanceof CellInfoGsm) {
                CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellinfo).getCellSignalStrength();
                dbm = cellSignalStrengthGsm.getDbm();
                signalStrength = "GSM";
            } else if (cellinfo instanceof CellInfoLte) {
                CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellinfo).getCellSignalStrength();
                signalStrength = "LTE";
                dbm = cellSignalStrengthLte.getDbm();
            }
            signalStrength = signalStrength + " : " + dbm;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return signalStrength;
    }

    // Okay this might be a valid single
    // Should be subscribed on IO thread
    public Single<String> bandwidth() {
        return Single.create(sub -> {
            String rateValue = "";
            try {
                String oneGBFile = "http://speedtest.tele2.net/1GB.zip";
                URL url = new URL(oneGBFile);

                InputStream is = url.openStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                long startBytes = TrafficStats.getTotalRxBytes();
                int size = 0;
                byte[] buf = new byte[1024];
                long startTime = System.currentTimeMillis();

                // download 5000kb
                while (size < 5000 && System.currentTimeMillis() - startTime < 15000) {
                    bis.read(buf);
                    size++;
                }

                long endTime = System.currentTimeMillis();
                long endBytes = TrafficStats.getTotalRxBytes();
                long totalTime = endTime - startTime;
                long totalBytes = endBytes - startBytes;
                double rate = Math.round(totalBytes * 8 / 1024 / ((double) totalTime / 1000));

                rateValue = String.valueOf(rate).concat(" Kbps");

            } catch (IOException e) {
                e.printStackTrace();
                // please always handle error
                sub.onError(e);
            } finally {
                sub.onSuccess(rateValue);
            }
        });
    }
}