package com.heaven7.util.extra.callback;

import com.heaven7.util.extra.collection.CopyOnWriteArray;

import java.util.ArrayList;

/**
 * simple manager for manage a array of items. support max
 * Created by heaven7 on 2016/12/19.
 *
 * @param <T> the data type,  may be a listener type.
 */
public abstract class CallbackManager<T> {

    private final CopyOnWriteArray<T> mList;
    private final int mMaxcapacity;

    /**
     * create CallbackManager with target capacity.
     *
     * @param maxCapacity the capacity.
     */
    public CallbackManager(int maxCapacity) {
        this.mList = new CopyOnWriteArray<T>() {
            @Override
            protected void trim(ArrayList<T> list) {
                onTrim(list, mMaxcapacity);
            }
        };
        this.mMaxcapacity = maxCapacity;
    }

    /**
     * create CallbackManager without init the list.
     */
    public CallbackManager() {
        this(Integer.MAX_VALUE);
    }

    /**
     * register the t and ensure it is the unique.
     *
     * @param t the t to register.
     * @return true if register success.
     */
    public boolean register(T t) {
        return register(t, true);
    }

    /**
     * register the  t to the pool.
     *
     * @param t        the t to register.
     * @param identity if ensure it is identity.
     * @return true if register success.
     */
    public boolean register(T t, boolean identity) {
        if (!identity || !mList.contains(t)) {
            return mList.size() <= mMaxcapacity && mList.add(t);
        }
        return false;
    }

    /**
     * unregister the t.
     *
     * @param t the t to unregister
     * @return true if unregister success.
     */
    public boolean unregister(T t) {
        return mList.remove(t);
    }

    /**
     * unregister the all t.
     */
    public void unregisterAll() {
        mList.clear();
    }

    /**
     *  get the size of the callbacks.
     * @return the size of the callbacks.
     */
    public int size() {
        return mList.size();
    }

    /**
     * dispatch the callback by target param.
     *
     * @param param the param to dispatch to .
     */
    public void dispatchCallback(Object param) {
        final CopyOnWriteArray.Access<T> access = mList.start();
        try {
            for (int i = 0; i < access.size(); i++) {
                final T t = access.get(i);
                dispatchCallbackImpl(t, param);
            }
        } finally {
            mList.end();
        }
    }

    /**
     * trim the list if you need. called after iteration. eg: {@link #dispatchCallback(Object)}.
     *
     * @param list the list to trim.
     * @param maxCapacity the max capacity of the this. default is {@link Integer#MAX_VALUE}.
     */
    protected void onTrim(ArrayList<T> list, int maxCapacity) {

    }

    /**
     * dispatch the param to the target item of T. this is called in {@link #dispatchCallback(Object)}.
     *
     * @param t     the t
     * @param param the param to dispatch.
     */
    protected abstract void dispatchCallbackImpl(T t, Object param);


    /**
     * a simple implements of CallbackManager that use {@link Callback}.
     */
    public static class SimpleCallbackManager extends CallbackManager<Callback> {
        @Override
        protected void dispatchCallbackImpl(Callback callback, Object param) {
            callback.handleCallback(param);
        }
    }
}
