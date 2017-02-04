package com.orozco.netreport.inject;

import com.orozco.netreport.ui.main.MainView;

import dagger.Subcomponent;

@PerView
@Subcomponent(modules = ViewModule.class)
public interface ViewComponent {
    void inject(MainView mainView);
}