package com.orozco.netreport.inject;

import android.content.Context;
import android.content.res.Resources;

import com.orozco.netreport.Idiyanale;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Idiyanale application;

    public ApplicationModule(Idiyanale application) {
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
}
