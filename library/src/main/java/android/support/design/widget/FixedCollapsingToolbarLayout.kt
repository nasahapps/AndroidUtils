package android.support.design.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Created by hhasan on 7/25/17.
 */
class FixedCollapsingToolbarLayout(context: Context, attrs: AttributeSet? = null) : CollapsingToolbarLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val oldInsets = mLastInsets
        mLastInsets = mLastInsets?.consumeSystemWindowInsets()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mLastInsets = oldInsets
    }

}