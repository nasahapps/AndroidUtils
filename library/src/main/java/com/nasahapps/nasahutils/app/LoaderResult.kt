package com.nasahapps.nasahutils.app

/**
 * Created by Hakeem on 2/14/16.
 */
class LoaderResult<D> {

    var data: D? = null
    var error: Throwable? = null

    constructor()

    constructor(error: Throwable) {
        this.error = error
    }

    constructor(data: D) {
        this.data = data
    }

}
