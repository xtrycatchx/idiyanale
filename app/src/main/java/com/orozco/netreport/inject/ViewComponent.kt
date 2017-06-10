package com.orozco.netreport.inject

import dagger.Subcomponent

@PerView
@Subcomponent(modules = arrayOf(ViewModule::class))
interface ViewComponent