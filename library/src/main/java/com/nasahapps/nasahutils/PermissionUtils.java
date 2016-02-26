package com.nasahapps.nasahutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhasan on 2/26/16.
 */
public class PermissionUtils {

    public static boolean isPermissionGranted(FragmentActivity activity, String permission, @StringRes int reasonRes, int requestCode) {
        return isPermissionGranted(activity, new String[]{permission}, activity.getString(reasonRes), requestCode);
    }

    public static boolean isPermissionGranted(FragmentActivity activity, String[] permissions, @StringRes int reasonRes, int requestCode) {
        return isPermissionGranted(activity, permissions, activity.getString(reasonRes), requestCode);
    }

    public static boolean isPermissionGranted(FragmentActivity activity, String permission, String reason, int requestCode) {
        return isPermissionGranted(activity, new String[]{permission}, reason, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isPermissionGranted(final FragmentActivity activity, String[] permissions, String reason, final int requestCode) {
        final List<String> deniedPermissions = new ArrayList<>();

        boolean rationaleNeeded = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    rationaleNeeded = true;
                }

                deniedPermissions.add(permission);
            }
        }

        if (rationaleNeeded) {
            // Explain why
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle("Permission Request")
                    .setMessage(reason)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // If user clicks yes, ask permission
                            requestPermission(activity, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.onRequestPermissionsResult(requestCode, new String[]{""}, new int[]{PackageManager.PERMISSION_DENIED});
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            activity.onRequestPermissionsResult(requestCode, new String[]{""}, new int[]{PackageManager.PERMISSION_DENIED});
                        }
                    })
                    .create();
            Utils.tintDialogButtons(dialog, activity);
            dialog.show();
            return false;
        } else if (!deniedPermissions.isEmpty()) {
            // If user didn't need explaining why but we still need permission, ask permission
            requestPermission(activity, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            return false;
        } else {
            // We already have permission
            return true;
        }
    }

    public static boolean isPermissionGrantedWithoutExplanation(Context c, String[] permissions, int requestCode) {
        final List<String> deniedPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(c, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }

        if (!deniedPermissions.isEmpty()) {
            requestPermission(c, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            return false;
        } else {
            // We already have permission
            return true;
        }
    }

    private static void requestPermission(Context c, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) c, permissions, requestCode);
    }

    public static void showSettingsSnackbar(View v, String title, String packageName) {
        if (Utils.isAtApiLevel(Build.VERSION_CODES.M)) {
            final Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package:" + packageName));
            if (Utils.hasValidAppToOpen(i, v.getContext())) {
                Snackbar.make(v, title, Snackbar.LENGTH_LONG)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.getContext().startActivity(i);
                            }
                        })
                        .setActionTextColor(Utils.getColorFromAttribute(v.getContext(), R.attr.colorAccent))
                        .show();
            }
        }
    }
}
