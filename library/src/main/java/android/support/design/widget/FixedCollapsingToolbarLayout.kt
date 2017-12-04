package android.support.design.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Hakeem on 8/10/17.
 */

class FixedCollapsingToolbarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : CollapsingToolbarLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val oldInsets = mLastInsets
        mLastInsets?.let {
            mLastInsets = it.consumeSystemWindowInsets()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mLastInsets = oldInsets
    }

}
