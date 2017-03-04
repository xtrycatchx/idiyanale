package com.orozco.netreport.model;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class Sources {

    private Sources() {

    }

    public static Observable<String> networkOperator(final Context context) {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> sub) {

                String networkOperator = null;

                try {
                    networkOperator = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                sub.onNext(networkOperator);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public static Observable<Device> device() {
        Observable<Device> observable = Observable.create(new Observable.OnSubscribe<Device>() {
            @Override
            public void call(Subscriber<? super Device> sub) {

                Device device = new Device(Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());

                sub.onNext(device);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public static Observable<String> imei(final Context context) {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> sub) {


                String imei = null;

                try {
                    ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sub.onNext(imei);
                    sub.onCompleted();
                }

            }
        });
        return observable;
    }

    public static Observable<String> signal(final Context context) {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> sub) {

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
                }


                sub.onNext(signalStrength);
                sub.onCompleted();
            }
        });
        return observable;
    }
}
