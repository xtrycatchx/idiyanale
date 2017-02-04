package com.orozco.netreport;

import android.app.Application;

import com.orozco.netreport.inject.ApplicationComponent;
import com.orozco.netreport.inject.ApplicationModule;
import com.orozco.netreport.inject.DaggerApplicationComponent;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 *
 * Idiyanale - A Filipino goddess of labor and good deeds. Just naming the app after her
 *
 */

public class Idiyanale extends Application {

    private ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.init();
    }

    private void init() {
        this.appComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.appComponent;
    }




}