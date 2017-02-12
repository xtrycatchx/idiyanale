package com.orozco.netreport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.orozco.netreport.model.Data;

/**
 * Paul Sydney Orozco (@xtrycatchx) on 4/2/17.
 */

public class SharedPrefUtil {

    private static final String DATA_KEY = "data";

    public static void createTempData(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DATA_KEY, new Gson().toJson(new Data()));
        editor.commit();

    }

    public static void saveTempData(Activity activity, Data data) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DATA_KEY, new Gson().toJson(data));
        editor.commit();

    }

    public static Data retrieveTempData(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String tempData = sharedPref.getString(DATA_KEY, "");
        return new Gson().fromJson(tempData, Data.class);
    }
}
