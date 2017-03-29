package com.orozco.netreport.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orozco.netreport.Idiyanale;
import com.orozco.netreport.inject.ActivityComponent;
import com.orozco.netreport.inject.ActivityModule;
import com.orozco.netreport.inject.ApplicationComponent;



import butterknife.ButterKnife;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        checkForUpdates();
    }

    protected ActivityComponent getActivityComponent() {
        ApplicationComponent applicationComponent =
                ((Idiyanale) getApplication()).getApplicationComponent();
        return applicationComponent.plus(new ActivityModule(this));
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
