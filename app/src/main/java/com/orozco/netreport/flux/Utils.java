package com.orozco.netreport.flux;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * @author A-Ar Andrew Concepcion
 */
public class Utils {
    public static final String MSG_ERROR_DEFAULT = "Something went wrong.";
    public static final String MSG_ERROR_NETWORK = "No network connection.";

    public AppError getError(Throwable throwable) {
//        FirebaseCrash.logcat(Log.ERROR, "FLUX ERROR", throwable.getMessage());
//        FirebaseCrash.report(throwable);
        if (!(throwable instanceof HttpException)) {
            return AppError.createNetwork(MSG_ERROR_NETWORK);
        }
        Response response = ((HttpException) throwable).response();
        if (response == null) {
            return AppError.createHttp(MSG_ERROR_DEFAULT);
        }
        return AppError.createHttp(response.code(), -1, MSG_ERROR_DEFAULT);
    }
}
