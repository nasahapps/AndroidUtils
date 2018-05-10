package com.nasahapps.nasahutils.app

import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater

/**
 * Created by Hakeem on 7/26/15.
 */
abstract class BaseFragment : Fragment() {

    protected val TAG = javaClass.simpleName
    protected var menu: Menu? = null

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }
}
