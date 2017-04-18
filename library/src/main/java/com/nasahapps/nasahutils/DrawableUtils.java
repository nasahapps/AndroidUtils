package com.nasahapps.nasahutils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by Hakeem on 8/19/16.
 */

public class DrawableUtils {

    public static Drawable tintDrawableForColorInt(Drawable drawable, @ColorInt int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        wrappedDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return wrappedDrawable;
    }

    public static Drawable tintDrawableForColorRes(Context c, Drawable drawable, @ColorRes int color) {
        return tintDrawableForColorInt(drawable, ContextCompat.getColor(c, color));
    }

    public static Drawable tintDrawableForColorRes(Context c, @DrawableRes int drawableRes, @ColorRes int color) {
        return tintDrawableForColorRes(c, ContextCompat.getDrawable(c, drawableRes), color);
    }

    public static Drawable tintDrawableForColorInt(Context c, @DrawableRes int drawableRes, @ColorInt int color) {
        return tintDrawableForColorInt(ContextCompat.getDrawable(c, drawableRes), color);
    }

}
