package com.heaven7.android.ipc.server;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;

/**
 * a weak handler
 * @param <T> the t to WeakReference
 */
class WeakHandler<T> extends Handler {

    private final WeakReference<T> mWeakRef;

    public WeakHandler(T t) {
        this.mWeakRef = new WeakReference<T>(t);
    }

    public WeakHandler(Looper looper, T t) {
        super(looper);
        this.mWeakRef = new WeakReference<T>(t);
    }

    public T get() {
        return mWeakRef.get();
    }

}
