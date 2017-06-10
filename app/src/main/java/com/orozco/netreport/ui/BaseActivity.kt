package com.orozco.netreport.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import butterknife.ButterKnife
import com.orozco.netreport.BASS
import com.orozco.netreport.flux.FluxActivity
import com.orozco.netreport.inject.ActivityComponent
import com.orozco.netreport.inject.ActivityModule

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

abstract class BaseActivity : FluxActivity() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(layoutRes)
        ButterKnife.bind(this)
    }

    val activityComponent: ActivityComponent
        get() {
            val applicationComponent = (application as BASS).applicationComponent
            return applicationComponent.plus(ActivityModule(this))
        }

    @get:LayoutRes
    protected abstract val layoutRes: Int


}
