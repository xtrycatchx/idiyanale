package com.orozco.netreport.post.api;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class ApiUtils {

    private ApiUtils() {
    }

    public static final String BASE_URL = "https://idinayale.herokuapp.com/api/";

    public static RestAPI getAPIService() {
        return RestClient.getClient(BASE_URL).create(RestAPI.class);
    }
}
