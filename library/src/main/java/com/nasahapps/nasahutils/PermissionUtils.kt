package com.nasahapps.nasahutils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import com.nasahapps.nasahutils.Utils.getColorFromAttribute
import com.nasahapps.nasahutils.Utils.isAtApiLevel
import com.nasahapps.nasahutils.Utils.tintButtons
import java.util.ArrayList

/**
 * Created by hhasan on 2/26/16.
 */
object PermissionUtils {

    @JvmStatic
    fun isPermissionGranted(activity: FragmentActivity, permission: String, @StringRes reasonRes: Int, requestCode: Int): Boolean {
        return isPermissionGranted(activity, arrayOf(permission), activity.getString(reasonRes), requestCode)
    }

    @JvmStatic
    fun isPermissionGranted(activity: FragmentActivity, permissions: Array<String>, @StringRes reasonRes: Int, requestCode: Int): Boolean {
        return isPermissionGranted(activity, permissions, activity.getString(reasonRes), requestCode)
    }

    @JvmStatic
    fun isPermissionGranted(activity: FragmentActivity, permission: String, reason: String, requestCode: Int): Boolean {
        return isPermissionGranted(activity, arrayOf(permission), reason, requestCode)
    }

    @JvmStatic
    fun isPermissionGranted(activity: FragmentActivity, permissions: Array<String>, reason: String, requestCode: Int): Boolean {
        val deniedPermissions = ArrayList<String>()

        var rationaleNeeded = false
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    rationaleNeeded = true
                }

                deniedPermissions.add(permission)
            }
        }

        if (rationaleNeeded) {
            // Explain why
            AlertDialog.Builder(activity)
                    .setTitle("Permission Request")
                    .setMessage(reason)
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        // If user clicks yes, ask permission
                        requestPermission(activity, deniedPermissions.toTypedArray(), requestCode)
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, which -> activity.onRequestPermissionsResult(requestCode, arrayOf(""), intArrayOf(PackageManager.PERMISSION_DENIED)) }
                    .setOnCancelListener { activity.onRequestPermissionsResult(requestCode, arrayOf(""), intArrayOf(PackageManager.PERMISSION_DENIED)) }
                    .create()
                    .tintButtons()
                    .show()
            return false
        } else if (!deniedPermissions.isEmpty()) {
            // If user didn't need explaining why but we still need permission, ask permission
            requestPermission(activity, deniedPermissions.toTypedArray(), requestCode)
            return false
        } else {
            // We already have permission
            return true
        }
    }

    @JvmStatic
    fun isPermissionGrantedWithoutExplanation(c: Context, permissions: Array<String>, requestCode: Int): Boolean {
        val deniedPermissions = ArrayList<String>()

        permissions.forEach {
            if (ContextCompat.checkSelfPermission(c, it) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(it)
            }
        }

        if (!deniedPermissions.isEmpty()) {
            requestPermission(c, deniedPermissions.toTypedArray(), requestCode)
            return false
        } else {
            // We already have permission
            return true
        }
    }

    private fun requestPermission(c: Context, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(c as Activity, permissions, requestCode)
    }

    @JvmStatic
    fun showSettingsSnackbar(v: View, title: String, packageName: String) {
        if (isAtApiLevel(Build.VERSION_CODES.M)) {
            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            i.data = Uri.parse("package:" + packageName)
            if (Utils.hasValidAppToOpen(i, v.context)) {
                Snackbar.make(v, title, Snackbar.LENGTH_LONG)
                        .setAction("Settings") { v -> v.context.startActivity(i) }
                        .setActionTextColor(v.context.getColorFromAttribute(R.attr.colorAccent))
                        .show()
            }
        }
    }
}
