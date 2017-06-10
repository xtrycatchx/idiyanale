package com.orozco.netreport.flux

import retrofit2.adapter.rxjava.HttpException

/**
 * @author A-Ar Andrew Concepcion
 */
class Utils {

    fun getError(throwable: Throwable): AppError {
        //        FirebaseCrash.logcat(Log.ERROR, "FLUX ERROR", throwable.getMessage());
        //        FirebaseCrash.report(throwable);
        if (throwable !is HttpException) {
            return AppError.createNetwork(MSG_ERROR_NETWORK)
        }
        val response = throwable.response() ?: return AppError.createHttp(MSG_ERROR_DEFAULT)
        return AppError.createHttp(response.code(), -1, MSG_ERROR_DEFAULT)
    }

    companion object {
        val MSG_ERROR_DEFAULT = "Something went wrong."
        val MSG_ERROR_NETWORK = "No network connection."
    }
}
