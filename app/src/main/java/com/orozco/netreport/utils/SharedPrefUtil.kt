package com.orozco.netreport.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.orozco.netreport.model.Data

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

object SharedPrefUtil {

    private val DATA_KEY = "data"

    fun clearTempData(activity: Activity) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(DATA_KEY, "")
        editor.commit()
    }

    fun saveTempData(activity: Activity, data: Data) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(DATA_KEY, Gson().toJson(data))
        editor.commit()

    }

    fun retrieveTempData(activity: Activity): Data? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val tempData = sharedPref.getString(DATA_KEY, "")
        return Gson().fromJson(tempData, Data::class.java)
    }
}
