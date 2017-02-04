package com.orozco.netreport.handler.location;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sydney on 4/2/17.
 */

public class LocationHelper {

    @Inject
    public LocationHelper() {
    }

    public Observable<String> executeRetrieveLocation() {


        return Observable.just("");
    }


}
