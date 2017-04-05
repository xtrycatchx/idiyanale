package com.orozco.netreport;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orozco.netreport.inject.ApplicationComponent;
import com.orozco.netreport.inject.ApplicationModule;
import com.orozco.netreport.inject.DaggerApplicationComponent;
import com.orozco.netreport.inject.RestModule;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 *
 */

public class BASS extends Application {

    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        this.appComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .restModule(new RestModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.appComponent;
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            MultiDex.install(this);
        } catch (RuntimeException ignore) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }

}