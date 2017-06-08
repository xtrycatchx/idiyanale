package com.orozco.netreport.inject

import android.content.Context
import android.content.res.Resources

import com.orozco.netreport.BASS
import com.orozco.netreport.flux.Utils
import com.orozco.netreport.model.Sources

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: BASS) {

    @Provides
    @PerApplication
    fun provideApplicationContext(): Context {
        return application
    }


    @Provides
    @PerApplication
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @Provides
    @PerApplication
    fun provideUtils(): Utils {
        return Utils()
    }

    @Provides
    @PerApplication
    fun provideSources(context: Context): Sources {
        return Sources(context)
    }
}
