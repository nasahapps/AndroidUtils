package com.nasahapps.nasahutils;

import android.util.Log;

/**
 * Created by hhasan on 3/24/15.
 * <p/>
 * Helper class for logging (only on debug builds)
 */
public class LogUtils {

    public static void logD(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message);
    }

    public static void logE(String tag, String message, Throwable e) {
        if (BuildConfig.DEBUG) {
            if (e != null)
                Log.e(tag, message, e);
            else
                Log.e(tag, message);
        }
    }

    public static void logE(String tag, String message) {
        logE(tag, message, null);
    }

}
