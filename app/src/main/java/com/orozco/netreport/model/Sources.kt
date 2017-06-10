package com.orozco.netreport.model

import android.content.Context
import android.net.TrafficStats
import android.os.Build
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import com.orozco.netreport.BuildConfig
import rx.Observable
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL

/**
 * Paul Sydney Orozco (@xtrycatchx) on 12/2/17.
 */

class Sources(private val context: Context) {

    fun networkOperator(): String {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
    }

    fun device(): Device {
        return Device(Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE, Build.VERSION_CODES::class.java.fields[android.os.Build.VERSION.SDK_INT].name)
    }

    fun imei(): String {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
    }

    fun version(): String {
        return BuildConfig.VERSION_NAME
    }


    fun signal(): String {
        var signalStrength = ""
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val cellinfo = telephonyManager.allCellInfo[0]
            var dbm = -666
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && cellinfo is CellInfoWcdma) {
                val cellSignalStrengthWcdma = cellinfo.cellSignalStrength
                dbm = cellSignalStrengthWcdma.dbm
                signalStrength = "WCDMA"
            } else if (cellinfo is CellInfoGsm) {
                val cellSignalStrengthGsm = cellinfo.cellSignalStrength
                dbm = cellSignalStrengthGsm.dbm
                signalStrength = "GSM"
            } else if (cellinfo is CellInfoLte) {
                val cellSignalStrengthLte = cellinfo.cellSignalStrength
                signalStrength = "LTE"
                dbm = cellSignalStrengthLte.dbm
            }
            signalStrength = "$signalStrength : $dbm"

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return signalStrength
    }

    // Okay this might be a valid observable
    // Should be subscribed on IO thread
    fun bandwidth(): Observable<String> {
        return Observable.create<String> { sub ->
            var rateValue = ""
            try {
                val oneGBFile = "http://mirror.pregi.net/centos/7/isos/x86_64/CentOS-7-x86_64-Everything-1611.iso"
                val url = URL(oneGBFile)

                val `is` = url.openStream()
                val bis = BufferedInputStream(`is`)
                val startBytes = TrafficStats.getTotalRxBytes()
                var size = 0
                val buf = ByteArray(1024)
                val startTime = System.currentTimeMillis()

                // download 5000kb
                while (size < 5000 && System.currentTimeMillis() - startTime < 15000) {
                    bis.read(buf)
                    size++
                }

                val endTime = System.currentTimeMillis()
                val endBytes = TrafficStats.getTotalRxBytes()
                val totalTime = endTime - startTime
                val totalBytes = endBytes - startBytes
                val rate = Math.round((totalBytes * 8).toDouble() / 1024.0 / (totalTime.toDouble() / 1000)).toDouble()

                rateValue = rate.toString() + " Kbps"

            } catch (e: IOException) {
                e.printStackTrace()
                // please always handle error
                sub.onError(e)
            } finally {
                sub.onNext(rateValue)
                sub.onCompleted()
            }
        }
    }
}