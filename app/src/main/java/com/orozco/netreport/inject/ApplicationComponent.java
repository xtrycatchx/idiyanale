package com.orozco.netreport.inject;

import dagger.Component;

@PerApplication
@Component(modules = {
        ApplicationModule.class,
        RestModule.class,
        ActionCreatorModule.class,
        ModelModule.class,
        StoreModule.class
})
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule activityModule);
}
