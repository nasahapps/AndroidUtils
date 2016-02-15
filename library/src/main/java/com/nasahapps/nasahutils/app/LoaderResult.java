package com.nasahapps.nasahutils.app;

/**
 * Created by Hakeem on 2/14/16.
 */
public class LoaderResult<D> {

    public D data;
    public Throwable error;

    public LoaderResult() {
    }

    public LoaderResult(Throwable error) {
        this.error = error;
    }

    public LoaderResult(D data) {
        this.data = data;
    }
}
