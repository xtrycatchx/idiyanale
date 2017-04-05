package com.orozco.netreport.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.orozco.netreport.BASS;
import com.orozco.netreport.flux.FluxActivity;
import com.orozco.netreport.inject.ActivityComponent;
import com.orozco.netreport.inject.ActivityModule;
import com.orozco.netreport.inject.ApplicationComponent;

import butterknife.ButterKnife;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public abstract class BaseActivity extends FluxActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
    }

    protected ActivityComponent getActivityComponent() {
        ApplicationComponent applicationComponent =
                ((BASS) getApplication()).getApplicationComponent();
        return applicationComponent.plus(new ActivityModule(this));
    }

    @LayoutRes
    protected abstract int getLayoutRes();


}
