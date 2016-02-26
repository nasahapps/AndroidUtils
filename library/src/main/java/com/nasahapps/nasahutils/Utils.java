package com.nasahapps.nasahutils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

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
                                 boolean cancelable,
                                 DialogInterface.OnClickListener onClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(c)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(buttonText, onClickListener);
            // If there's a click listener for the positive button, we need to know if the dialog
            // should be cancellable or not
            // If there's not a click listener, then clicking the positive button would just
            // close the dialog, negating the need for a cancel button anyway
            if (onClickListener != null) {
                if (cancelable) {
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else {
                    builder.setCancelable(false);
                }
            }
            builder.show();
        } catch (Exception e) {
            // Catching a BadTokenException, which randomly happens sometimes
        }
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

    /**
     * Hides the keyboard if showing
     *
     * @param c    Context
     * @param view the view that currently has focus and requires the keyboard's attention, e.g. an EditText
     */
    public static void hideKeyboard(Context c, View view) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Shows the keyboard if hidden
     *
     * @param c    Context
     * @param view the view that would call for the keyboard to appear, e.g. an EditText
     */
    public static void showKeyboard(Context c, View view) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
    }

    /**
     * Get the screen dimensions in pixels
     *
     * @param a Activity
     * @return a Point, where the width is Point.x and height is Point.y
     */
    public static Point getScreenDimensions(Activity a) {
        if (a != null) {
            Display d = a.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            d.getSize(size);
            return size;
        } else return new Point();
    }

    /**
     * Get the screen width
     *
     * @param activity Activity
     * @return screen width in pixels
     */
    public static int getScreenWidth(Activity activity) {
        if (activity != null) {
            return getScreenDimensions(activity).x;
        } else return 0;
    }

    /**
     * Get the screen height
     *
     * @param activity Activity
     * @return screen height in pixels
     */
    public static int getScreenHeight(Activity activity) {
        if (activity != null) {
            return getScreenDimensions(activity).y;
        } else return 0;
    }

    /**
     * Get the overall parent view of this activity, since there's no getView() method like Fragments have
     *
     * @param a Activity
     * @return the topmost parent view
     */
    @Nullable
    public static View getActivityView(Activity a) {
        if (a != null) {
            View content = a.findViewById(android.R.id.content);
            if (content != null && content instanceof ViewGroup && ((ViewGroup) content).getChildCount() > 0) {
                return ((ViewGroup) content).getChildAt(0);
            }
        }

        return null;
    }

    /**
     * Checks if user has an app installed to handle the given Intent
     *
     * @param i Intent to start
     * @param c Context
     * @return true if user has an app to handle the Intent, false otherwise
     */
    public static boolean hasValidAppToOpen(Intent i, Context c) {
        return i.resolveActivity(c.getPackageManager()) != null;
    }

    /**
     * Checks if the device is currently connected to the internet
     *
     * @param c context
     * @return true if connected or connecting to the internet
     */
    public static boolean isConnectedToInternet(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     * Tints the Dialog buttons to the primary color of the app.
     * This is automatically done on 6.0+, so this method is mainly for 5.1 and below.
     *
     * @param dialog  the Dialog to tint the buttons of
     * @param context Context
     * @return the same Dialog but now with tinted buttons
     */
    public static AlertDialog tintDialogButtons(final AlertDialog dialog, final Context context) {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                if (dialog.getButton(DialogInterface.BUTTON_POSITIVE) != null) {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColorFromAttribute(context, R.attr.colorPrimary));
                }

                if (dialog.getButton(DialogInterface.BUTTON_NEUTRAL) != null) {
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getColorFromAttribute(context, R.attr.colorPrimary));
                }

                if (dialog.getButton(DialogInterface.BUTTON_NEGATIVE) != null) {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColorFromAttribute(context, R.attr.colorPrimary));
                }
            }
        });

        return dialog;
    }

    /**
     * Retrieves a color int from an attribute, e.g. R.attr.colorAccent
     *
     * @param c   context
     * @param res attribute referencing the color
     * @return the color int of the given attribute, or 0x0 (black) if the attribute does not exist
     * or doesn't reference a color resource
     */
    @ColorInt
    public static int getColorFromAttribute(Context c, @AttrRes int res) {
        try {
            TypedValue tv = new TypedValue();
            TypedArray ta = c.obtainStyledAttributes(tv.data, new int[]{res});
            int color = ta.getColor(0, 0);
            ta.recycle();
            return color;
        } catch (Exception e) {
            LogUtils.logE(Utils.class.getSimpleName(), "Error getting color from attribute", e);
            return 0;
        }
    }

}
