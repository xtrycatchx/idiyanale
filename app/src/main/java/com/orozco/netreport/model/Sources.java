package com.orozco.netreport.model;

import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class Sources {

    private Sources() {

    }

    public static Single<String> networkOperator(final Context context) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> sub) {

                String networkOperator = null;

                try {
                    networkOperator = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
                } catch (Exception e) {

                    e.printStackTrace();
                }
                sub.onSuccess(networkOperator);
            }
        });
    }

    public static Single<Device> device() {
        return Single.create(new Single.OnSubscribe<Device>() {
            @Override
            public void call(SingleSubscriber<? super Device> sub) {

                Device device = new Device(Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());

                sub.onSuccess(device);
            }
        });
    }

    public static Single<String> imei(final Context context) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> sub) {
                String imei = null;
                try {
                    imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sub.onSuccess(imei);
                }
            }
        });
    }


    public static Single<String> signal(final Context context) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> sub) {
                String signalStrength = new String();
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
                } finally {
                    sub.onSuccess(signalStrength);
                }
            }
        });
    }

    public static Single<String> bandwidth() {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> sub) {
                String ratevalue = "";
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

                    ratevalue = String.valueOf(rate).concat(" Kbps");

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    sub.onSuccess(ratevalue);
                }
            }
        });
    }
    //    public static Single<String> bandwidth(final Context context) {
//        return Single.create(new Single.OnSubscribe<String>() {
//            @Override
//            public void call(SingleSubscriber<? super String> sub) {
//                String ratevalue = new String();
//                try {
//                    String fiveMbFile = "http://d1355990.i49.quadrahosting.com.au/2012_06/M16&M17_NII.jpg";
//                    URL url = new URL(fiveMbFile);
//
//                    InputStream is = url.openConnection().getInputStream();
//                    BufferedInputStream bis = new BufferedInputStream(is);
//                    int red = 0;
//                    long size = 0;
//                    byte[] buf = new byte[1024];
//                    long startTime = System.currentTimeMillis();
//                    while ((red = bis.read(buf)) != -1) {
//                        size += red;
//                    }
//                    long endTime = System.currentTimeMillis();
//                    double rate = (((size / 1024) / ((endTime - startTime) / 1000)) * 8);
//                    rate = Math.round(rate * 100.0) / 100.0;
//                    //if (rate > 1000) {
//                    //    ratevalue = String.valueOf(rate / 1024).concat(" Mbps");
//                    //} else {
//                    ratevalue = String.valueOf(rate).concat(" Kbps");
//                    //}
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    sub.onSuccess(ratevalue);
//                }
//            }
//        });
//    }

}