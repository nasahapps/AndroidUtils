package com.nasahapps.nasahutils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;

/**
 * Created by Hakeem on 7/26/15.
 */
public class Utils {

    /**
     * Checks if the device is at least a 7-in tablet.
     * More specifically, it checks if the screen's smallest screen width is at least 600 dp.
     * @param c context
     * @return true if the neither the width nor length of a device's screen is at least 600 dp
     */
    public static boolean isTablet(Context c) {
        int dp = c.getResources().getConfiguration().smallestScreenWidthDp;
        return dp >= 600;
    }

    /**
     * Checks if the device is a 10-in tablet (at least 720 dp)
     *
     * @param c Context
     * @return true if 10 inches (like Nexus 10), false if under
     */
    public static boolean is10Inches(Context c) {
        int dp = c.getResources().getConfiguration().smallestScreenWidthDp;
        return dp >= 720;
    }

    /**
     * Checks if the device is currently in portrait mode
     *
     * @param c Context
     * @return true if in portrait, false if landscape
     */
    public static boolean isPortrait(Context c) {
        return c.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isAtApiLevel(int api) {
        return Build.VERSION.SDK_INT >= api;
    }

    public static void showAlert(Context c, String title, String message, String buttonText,
                                 DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(c)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, onClickListener)
                .show();
    }

    /**
     * Convert DP units to pixels
     *
     * @param c  Context
     * @param dp DP to convert
     * @return pixels
     */
    public static int dpToPixel(Context c, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                c.getResources().getDisplayMetrics());
    }

    /**
     * Used to clear the Activity stack and so the next called Activity
     * will be at the very bottom of the stack, and pressing "back" will end the app
     * instead of going to the previous Activity
     *
     * @param i Intent to start
     * @return the same Intent
     */
    public static Intent clearActivityStack(Intent i) {
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

}
