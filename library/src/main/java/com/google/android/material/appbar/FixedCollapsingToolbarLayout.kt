package com.google.android.material.appbar

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Hakeem on 8/10/17.
 */

class FixedCollapsingToolbarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : CollapsingToolbarLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val oldInsets = lastInsets
        lastInsets?.let {
            lastInsets = it.consumeSystemWindowInsets()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        lastInsets = oldInsets
    }

}
