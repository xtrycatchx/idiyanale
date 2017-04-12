package com.orozco.netreport.model;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import rx.Observable;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

public class Sources {

    private final Context context;

    public Sources(Context context) {
        this.context = context;
    }

    public String networkOperator() {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
    }

    public Device device() {
        return new Device(Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName());
    }

    public String imei() {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }


    public String signal() {
        String signalStrength = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            CellInfo cellinfo = telephonyManager.getAllCellInfo().get(0);
            int dbm = -666;
            if (cellinfo instanceof CellInfoWcdma) {
                CellSignalStrengthWcdma cellSignalStrengthWcdma = ((CellInfoWcdma) cellinfo).getCellSignalStrength();
                dbm = cellSignalStrengthWcdma.getDbm();
                signalStrength = "WCDMA";
            } else if (cellinfo instanceof CellInfoGsm) {
                CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellinfo).getCellSignalStrength();
                dbm = cellSignalStrengthGsm.getDbm();
                signalStrength = "GSM";
            } else if (cellinfo instanceof CellInfoLte) {
                CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte) cellinfo).getCellSignalStrength();
                signalStrength = "LTE";
                dbm = cellSignalStrengthLte.getDbm();
            }
            signalStrength = signalStrength + " : " + dbm;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return signalStrength;
    }

    // Okay this might be a valid observable
    // Should be subscribed on IO thread
    public Observable<String> bandwidth() {
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();

        return Observable.create(sub -> {
            // add a listener to wait for speedtest completion and progress
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onDownloadFinished(SpeedTestReport report) {
                    // called when download is finished
                    System.out.println("[DL FINISHED] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[DL FINISHED] rate in bit/s   : " + report.getTransferRateBit());
                    sub.onNext((speedTestSocket.getLiveDownloadReport().getTransferRateBit().intValue() / 1024)  + " Kbps");
                    sub.onCompleted();
                }

                @Override
                public void onDownloadError(SpeedTestError speedTestError, String errorMessage) {
                    // called when a download error occur
                    sub.onError(new Throwable(errorMessage));
                }

                @Override
                public void onUploadFinished(SpeedTestReport report) {
                    // called when an upload is finished
                    System.out.println("[UL FINISHED] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[UL FINISHED] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onUploadError(SpeedTestError speedTestError, String errorMessage) {
                    // called when an upload error occur
                }

                @Override
                public void onDownloadProgress(float percent, SpeedTestReport report) {
                    // called to notify download progress
                    if (percent >= 0.12) { // 0.12% of 7.7GB = 10MB
                        speedTestSocket.forceStopTask();
                    }
                    System.out.println("[DL PROGRESS] progress : " + percent + "%");
                    System.out.println("[DL PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[DL PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onUploadProgress(float percent, SpeedTestReport report) {
                    // called to notify upload progress
                    System.out.println("[UL PROGRESS] progress : " + percent + "%");
                    System.out.println("[UL PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[UL PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onInterruption() {
                    // triggered when forceStopTask is called
                    sub.onNext((speedTestSocket.getLiveDownloadReport().getTransferRateBit().intValue() / 1024)  + " Kbps");
                    sub.onCompleted();
                }
            });

            speedTestSocket.startFtpFixedDownload("mirror.pregi.net", "/centos/7/isos/x86_64/CentOS-7-x86_64-Everything-1611.iso", 10000);
        });
    }
}