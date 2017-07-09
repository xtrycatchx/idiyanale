package com.orozco.netreport.inject

import com.orozco.netreport.BASS
import dagger.Component

@PerApplication
@Component(modules = arrayOf(ApplicationModule::class, RestModule::class, ActionCreatorModule::class, ModelModule::class, StoreModule::class))
interface ApplicationComponent {
    fun plus(activityModule: ActivityModule): ActivityComponent
    fun inject(app: BASS)

}
