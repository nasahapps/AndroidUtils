package android.support.design.widget;

import android.content.Context;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;

/**
 * Created by Hakeem on 8/10/17.
 */

public class FixedCollapsingToolbarLayout extends CollapsingToolbarLayout {

    public FixedCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public FixedCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowInsetsCompat oldInsets = mLastInsets;
        if (mLastInsets != null) {
            mLastInsets = mLastInsets.consumeSystemWindowInsets();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLastInsets = oldInsets;
    }
}
