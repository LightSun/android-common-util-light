package com.heaven7.util.extra.callback;

import com.heaven7.util.extra.collection.CopyOnWriteArray;

import java.util.ArrayList;

/**
 * simple manager for manage a array of items. support limit count.
 * Created by heaven7 on 2016/12/19.
 *
 * @param <T> the data type,  may be a listener type.
 * @since 1.0.9
 */
public abstract class CallbackManager<T> {

    protected final CopyOnWriteArray<T> mList;
    private final int mMaxcapacity;

    private final SameItemMatcher mSameItemMatcher = new SameItemMatcher();
    private final CopyOnWriteArray.ElementVisitor<T> mDispatchVisitor = new CopyOnWriteArray.ElementVisitor<T>() {
        @Override
        public boolean visit(T t, Object param) {
            dispatchCallbackImpl(t, param);
            return true;
        }
    };


    /**
     * create CallbackManager with target capacity.
     *
     * @param maxCapacity the capacity.
     */
    public CallbackManager(int maxCapacity) {
        this.mMaxcapacity = maxCapacity;
        this.mList = new CopyOnWriteArray<T>() {
            @Override
            protected void trim(ArrayList<T> list) {
                onTrim(list, mMaxcapacity);
            }
        };
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
     * @param t1       the t to register.
     * @param identity if ensure it is identity.
     * @return true if register success.
     */
    public boolean register(T t1, boolean identity) {
        if (t1 == null) {
            throw new NullPointerException();
        }
        mSameItemMatcher.setIdentity(identity);
        final boolean success = mList.acceptVisit(CopyOnWriteArray.VISIT_RULE_UNTIL_SUCCESS,
                t1, mSameItemMatcher);
        if (success) {
            return mList.size() <= mMaxcapacity && mList.add(t1);
        }
        return false;
    }

    /**
     * unregister the t.
     *
     * @param t the t to unregister
     * @return true if unregister success.
     */
    public boolean unregister(T t, boolean identity) {
        if (t == null) {
            return false;
        }
        mSameItemMatcher.setIdentity(identity);
        final T result = mList.find(t, mSameItemMatcher);
        return result != null && mList.remove(result);
    }

    /**
     * unregister the  t.
     *
     * @param t the t
     * @return true if unregister success.
     */
    public boolean unregister(T t) {
        return unregister(t, false);
    }

    /**
     * unregister the all t.
     */
    public void unregisterAll() {
        mList.clear();
    }

    /**
     * get the size of the callbacks.
     *
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
        mList.acceptVisit(CopyOnWriteArray.VISIT_RULE_ALL, param, mDispatchVisitor);
    }

    /**
     * are the same items.
     *
     * @param t        the item .
     * @param other    the other item
     * @param identity if identity
     * @return true if the t and the other is the same item
     * @since 1.1.0
     */
    protected boolean areItemsTheSame(T t, Object other, boolean identity) {
        if (identity) {
            return t == other;
        }
        return t.equals(other);
    }

    /**
     * trim the list if you need. called after iteration. eg: {@link #dispatchCallback(Object)}.
     *
     * @param list        the current list of callbacks to trim.
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


    private class SameItemMatcher implements CopyOnWriteArray.ElementVisitor<T>,
            CopyOnWriteArray.ElementPredicate<T> {
        boolean identity;

        public SameItemMatcher() {
        }

        public void setIdentity(boolean identity) {
            this.identity = identity;
        }

        @Override
        public boolean visit(T t, Object param) {
            return areItemsTheSame(t, param, identity);
        }

        @Override
        public boolean test(T t, Object param) {
            return areItemsTheSame(t, param, identity);
        }
    }

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
