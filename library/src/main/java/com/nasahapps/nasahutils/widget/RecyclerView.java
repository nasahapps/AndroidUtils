package com.nasahapps.nasahutils.widget;

import android.view.View;

/**
 * Created by Hakeem on 5/7/16.
 */
public class RecyclerView {


    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    public static abstract class Adapter extends android.support.v7.widget.RecyclerView.Adapter {

        protected OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

    }
}
