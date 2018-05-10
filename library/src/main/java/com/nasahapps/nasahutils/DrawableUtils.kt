package com.nasahapps.nasahutils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Created by Hakeem on 8/19/16.
 */

object DrawableUtils {

    @JvmStatic
    fun tintDrawableForColorInt(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it.mutate())
            wrappedDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            return wrappedDrawable
        }

        return null
    }

    @JvmStatic
    fun tintDrawableForColorRes(c: Context, drawable: Drawable?, @ColorRes color: Int): Drawable? {
        return tintDrawableForColorInt(drawable, ContextCompat.getColor(c, color))
    }

    @JvmStatic
    fun tintDrawableForColorRes(c: Context, @DrawableRes drawableRes: Int, @ColorRes color: Int): Drawable? {
        return tintDrawableForColorRes(c, ContextCompat.getDrawable(c, drawableRes), color)
    }

    @JvmStatic
    fun tintDrawableForColorInt(c: Context, @DrawableRes drawableRes: Int, @ColorInt color: Int): Drawable? {
        return tintDrawableForColorInt(ContextCompat.getDrawable(c, drawableRes), color)
    }

}