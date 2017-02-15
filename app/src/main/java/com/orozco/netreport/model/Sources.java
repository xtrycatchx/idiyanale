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

import com.orozco.netreport.utils.ConnectionTypeUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class Sources {

    /**public Observable<DataTemp> carrier(final Context context) {
        Observable<DataTemp> observable = Observable.create(new Observable.OnSubscribe<DataTemp>() {
            @Override
            public void call(Subscriber<? super DataTemp> sub) {
                DataTemp temp = new DataTemp(DataEnum.CARRIER, ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName());
                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public Observable<DataTemp> phoneModel() {
        Observable<DataTemp> observable = Observable.create(new Observable.OnSubscribe<DataTemp>() {
            @Override
            public void call(Subscriber<? super DataTemp> sub) {
                DataTemp temp = new DataTemp(DataEnum.PHONE_MODEL, Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());
                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public Observable<DataTemp> oSversion() {
        Observable<DataTemp> observable = Observable.create(new Observable.OnSubscribe<DataTemp>() {
            @Override
            public void call(Subscriber<? super DataTemp> sub) {
                DataTemp temp = new DataTemp(DataEnum.OSVERSION, android.os.Build.VERSION.RELEASE);
                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public Observable<DataTemp> imei(final Context context) {
        Observable<DataTemp> observable = Observable.create(new Observable.OnSubscribe<DataTemp>() {
            @Override
            public void call(Subscriber<? super DataTemp> sub) {
                DataTemp temp = new DataTemp(DataEnum.IMEI, ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public Observable<DataTemp> connection(final Context context) {
        Observable<DataTemp> observable = Observable.create(new Observable.OnSubscribe<DataTemp>() {
            @Override
            public void call(Subscriber<? super DataTemp> sub) {
                DataTemp temp = new DataTemp(DataEnum.CONNECTION, ConnectionTypeUtil.getConnectionType(context));
                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }

    public Observable<DataTemp> signal(final Context context) {
        Observable<DataTemp> observable = Observable.create(new Observable.OnSubscribe<DataTemp>() {
            @Override
            public void call(Subscriber<? super DataTemp> sub) {

                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                CellInfo cellinfo = telephonyManager.getAllCellInfo().get(0);
                String signalStrength = new String();
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

                DataTemp temp = new DataTemp(DataEnum.SIGNAL, signalStrength);

                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }**/
}
