package com.orozco.netreport.model;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orozco.netreport.model.Coordinates;
import com.orozco.netreport.model.Data;
import com.orozco.netreport.model.DataEnum;
import com.orozco.netreport.model.DataTemp;
import com.orozco.netreport.utils.ConnectionTypeUtil;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class Sources {

    public Observable<DataTemp> carrier(final Context context) {
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
                DataTemp temp = new DataTemp(DataEnum.CONNECTION, "");//ConnectionTypeUtil.getConnectionType(context));
                sub.onNext(temp);
                sub.onCompleted();
            }
        });
        return observable;
    }
}
