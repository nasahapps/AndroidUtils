package com.nasahapps.nasahutils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

/**
 * Created by Hakeem on 7/26/15.
 */
object Utils {

    /**
     * Checks if user has an app installed to handle the given Intent
     *
     * @param i Intent to start
     * @param c Context
     * @return true if user has an app to handle the Intent, false otherwise
     */
    @JvmStatic
    fun hasValidAppToOpen(i: Intent, c: Context): Boolean {
        return i.resolveActivity(c.packageManager) != null
    }

}

/**
 * Checks if the device is at least a 7-in tablet.
 * More specifically, it checks if the screen's smallest screen width is at least 600 dp.
 * @return true if the neither the width nor length of a device's screen is at least 600 dp
 */
fun Context?.isTablet(): Boolean {
    this?.let {
        val dp = resources.configuration.smallestScreenWidthDp
        return dp >= 600
    }

    return false
}

/**
 * Checks if the device is a 10-in tablet (at least 720 dp)
 * @return true if 10 inches (like Nexus 10), false if under
 */
fun Context?.is10Inches(): Boolean {
    this?.let {
        val dp = resources.configuration.smallestScreenWidthDp
        return dp >= 720
    }

    return false
}

/**
 * Checks if the device is currently in portrait mode
 * @return true if in portrait, false if landscape
 */
fun Context?.isPortrait(): Boolean {
    return this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun Any?.isAtApiLevel(api: Int): Boolean {
    return Build.VERSION.SDK_INT >= api
}

fun Context?.showAlert(title: String, message: String, buttonText: String, cancelable: Boolean,
                       onClickListener: DialogInterface.OnClickListener?) {
    this?.let {
        try {
            val builder = AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(buttonText, onClickListener)
            // If there's a click listener for the positive button, we need to know if the dialog
            // should be cancellable or not
            // If there's not a click listener, then clicking the positive button would just
            // close the dialog, negating the need for a cancel button anyway
            if (onClickListener != null) {
                if (cancelable) {
                    builder.setNegativeButton(android.R.string.cancel) { dialog, which -> }
                } else {
                    builder.setCancelable(false)
                }
            }
            builder.show()
        } catch (e: Exception) {
            // Catching a BadTokenException, which randomly happens sometimes
        }
    }
}

/**
 * Convert DP units to pixels
 *
 * @param dp DP to convert
 * @return pixels
 */
fun Context?.dpToPixel(dp: Int): Int {
    this?.let {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                resources.displayMetrics).toInt()
    }

    return dp
}

/**
 * Used to clear the Activity stack and so the next called Activity
 * will be at the very bottom of the stack, and pressing "back" will end the app
 * instead of going to the previous Activity
 *
 * @return the same Intent
 */
fun Intent.clearActivityStack(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
            Intent.FLAG_ACTIVITY_SINGLE_TOP or
            Intent.FLAG_ACTIVITY_CLEAR_TASK or
            Intent.FLAG_ACTIVITY_NEW_TASK)
    return this
}

/**
 * Hides the keyboard if showing
 */
fun View?.hideKeyboard() {
    this?.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

/**
 * Shows the keyboard if hidden
 */
fun View?.showKeyboard() {
    this?.let {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInputFromInputMethod(windowToken, InputMethodManager.SHOW_FORCED)
    }
}

/**
 * Get the screen dimensions in pixels
 * @return a Point, where the width is Point.x and height is Point.y
 */
fun Activity?.getScreenDimensions(): Point {
    if (this != null) {
        val d = windowManager.defaultDisplay
        val size = Point()
        d.getSize(size)
        return size
    } else
        return Point()
}

/**
 * Get the screen width
 * @return screen width in pixels
 */
fun Activity?.getScreenWidth(): Int {
    return this?.getScreenDimensions()?.x ?: 0
}

/**
 * Get the screen height
 * @return screen height in pixels
 */
fun Activity?.getScreenHeight(): Int {
    return this?.getScreenDimensions()?.y ?: 0
}

/**
 * Get the overall parent view of this activity, since there's no getView() method like Fragments have
 * @return the topmost parent view
 */
fun Activity?.getActivityView(): View? {
    if (this != null) {
        val content = findViewById<View?>(android.R.id.content)
        if (content is ViewGroup && content.childCount > 0) {
            return content.getChildAt(0)
        }
    }

    return null
}

/**
 * Checks if the device is currently connected to the internet
 * @return true if connected or connecting to the internet
 */
fun Context?.isConnectedToInternet(): Boolean {
    this?.let {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnectedOrConnecting
    }

    return false
}

/**
 * Tints the Dialog buttons to the primary color of the app.
 * This is automatically done on 6.0+, so this method is mainly for 5.1 and below.
 * @param context Context
 * @return the same Dialog but now with tinted buttons
 */
fun AlertDialog.tintButtons(): AlertDialog {
    setOnShowListener {
        getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(context.getColorFromAttribute(R.attr.colorPrimary))
        getButton(DialogInterface.BUTTON_NEUTRAL)?.setTextColor(context.getColorFromAttribute(R.attr.colorPrimary))
        getButton(DialogInterface.BUTTON_NEGATIVE)?.setTextColor(context.getColorFromAttribute(R.attr.colorPrimary))
    }

    return this
}

/**
 * Retrieves a color int from an attribute, e.g. R.attr.colorAccent
 * @param res attribute referencing the color
 * @return the color int of the given attribute, or 0x0 (black) if the attribute does not exist
 * or doesn't reference a color resource
 */
@ColorInt
fun Context?.getColorFromAttribute(@AttrRes res: Int): Int {
    this?.let {
        try {
            val tv = TypedValue()
            val ta = obtainStyledAttributes(tv.data, intArrayOf(res))
            val color = ta.getColor(0, 0)
            ta.recycle()
            return color
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    return 0
}

fun Context?.getDrawableFromAttribute(@AttrRes res: Int): Drawable? {
    this?.let {
        try {
            val tv = TypedValue()
            val ta = obtainStyledAttributes(tv.data, intArrayOf(res))
            val drawable = ta.getDrawable(0)
            ta.recycle()
            return drawable
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    return null
}