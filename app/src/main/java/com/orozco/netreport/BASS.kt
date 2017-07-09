package com.orozco.netreport

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.evernote.android.job.JobManager
import com.orozco.netreport.inject.ApplicationComponent
import com.orozco.netreport.inject.ApplicationModule
import com.orozco.netreport.inject.DaggerApplicationComponent
import com.orozco.netreport.inject.RestModule
import com.orozco.netreport.service.BASSJobCreator
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.

 */

class BASS : Application() {

    lateinit var applicationComponent: ApplicationComponent
    @Inject lateinit var bassJobCreator: BASSJobCreator

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        Fabric.with(this, Crashlytics())
        this.applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .restModule(RestModule())
                .build()
        applicationComponent.inject(this)
        JobManager.create(this).addJobCreator(bassJobCreator)
    }

    public override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        try {
            MultiDex.install(this)
        } catch (ignore: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }

    }

}