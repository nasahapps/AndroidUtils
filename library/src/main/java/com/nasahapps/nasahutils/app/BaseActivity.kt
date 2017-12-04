package com.nasahapps.nasahutils.app

import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

/**
 * Created by Hakeem on 7/26/15.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected val TAG = javaClass.simpleName
    protected var menu: Menu? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
