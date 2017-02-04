package com.orozco.netreport.inject;

import dagger.Component;

@PerApplication
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule activityModule);
}
