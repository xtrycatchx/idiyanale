package com.orozco.netreport.inject;

import android.content.Context;
import android.content.res.Resources;

import com.orozco.netreport.BASS;
import com.orozco.netreport.flux.Utils;
import com.orozco.netreport.model.Sources;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final BASS application;

    public ApplicationModule(BASS application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    public Context provideApplicationContext() {
        return application;
    }


    @Provides
    @PerApplication
    public Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    @PerApplication
    public Utils provideUtils() {
        return new Utils();
    }

    @Provides
    @PerApplication
    public Sources provideSources(Context context) {
        return new Sources(context);
    }
}
