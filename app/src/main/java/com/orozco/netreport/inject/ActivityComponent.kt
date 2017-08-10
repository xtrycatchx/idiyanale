package com.orozco.netreport.inject

import com.orozco.netreport.ui.history.HistoryActivity
import com.orozco.netreport.ui.main.MainActivity

import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    operator fun plus(viewModule: ViewModule): ViewComponent
    fun inject(activity: MainActivity)
    fun inject(activity: HistoryActivity)
}
