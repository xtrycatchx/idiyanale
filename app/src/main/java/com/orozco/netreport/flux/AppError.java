package com.orozco.netreport.flux;

/**
 * @author A-Ar Andrew Concepcion
 */
public class AppError {
    private int statusCode;
    private int errorCode;
    private String errorMessage;
    private Boolean isNetwork;

    public AppError(int statusCode, int errorCode, String errorMessage, Boolean isNetwork) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.isNetwork = isNetwork;
    }

    public static AppError createNetwork(String errorMessage) {
      return new AppError(-1, -1, errorMessage, true);
    }
    public static AppError createHttp(String errorMessage) {
      return new AppError(-1, -1, errorMessage, false);
    }
    public static AppError createHttp(int statusCode, int errorCode, String errorMessage) {
      return new AppError(statusCode, errorCode, errorMessage, false);
    }
}