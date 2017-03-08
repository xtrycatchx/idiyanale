package com.orozco.netreport.common;

import com.orozco.netreport.inject.PerApplication;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;

@PerApplication
public class Schedulers {

    @Inject
    public Schedulers() {
    }

    public Scheduler getMainThread() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler getBackgroundThread() {
        return rx.schedulers.Schedulers.io();
    }

    public <T> Single.Transformer<T, T> apply() {
        return new Single.Transformer<T, T>() {
            @Override
            public Single<T> call(Single<T> observable) {
                return observable.subscribeOn(getBackgroundThread()).observeOn(getMainThread());
            }
        };
    }
}
