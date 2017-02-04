package com.orozco.netreport.inject;

import com.orozco.netreport.ui.main.MainActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    ViewComponent plus(ViewModule viewModule);

    void inject(MainActivity activity);
}
