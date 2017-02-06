package com.orozco.netreport.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.orozco.netreport.Idiyanale;
import com.orozco.netreport.inject.ActivityComponent;
import com.orozco.netreport.inject.ActivityModule;
import com.orozco.netreport.inject.ApplicationComponent;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

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
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}
