package com.heaven7.util.extra.subscribe;

import android.support.v4.util.ArrayMap;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * the subscribe manager help we subscribe  items.
 *
 * @param <T>
 */
public class SubscribeManager<T> {

    // private static final String TAG = "SubscribeManager";
    /**
     * the subscribed items.
     */
    private final ArrayList<T> mSubscribedItems = new ArrayList<>();
    /**
     * the map which map the item with the view.
     */
    private final ArrayMap<T, View> mMap;
    private final OnSubscribeStateChangeListener<? super T> mListener;

    public SubscribeManager(OnSubscribeStateChangeListener<? super T> l) {
        this.mMap = new ArrayMap<>();
        this.mListener = l;
    }

    /**
     * unbind the items.
     */
    public void unbind() {
        mMap.clear();
    }

    /**
     * bind the item to the view
     *
     * @param item the item
     * @param view the view.
     */
    public void bind(T item, View view) {
        mMap.put(item, view);
    }

    /**
     * check the subscribe state.
     *
     * @param item the item to check.
     */
    public void checkSubscribeState(T item) {
        mListener.onSubscribeStateChange(mMap.get(item), item, mSubscribedItems.contains(item));
    }

    /**
     * cancel the subscribe by the target item.
     *
     * @return true if cancel subscribe success.
     */
    public boolean cancelSubscribe(T item) {
        if (mSubscribedItems.remove(item)) {
            mListener.onSubscribeStateChange(mMap.get(item), item, false);
            return true;
        }
        return false;
    }

    /**
     * subscribe the item
     *
     * @return true if subscribe the item success.
     */
    public boolean subscribe(T item) {
        if (!mSubscribedItems.contains(item)) {
            mSubscribedItems.add(item);
            mListener.onSubscribeStateChange(mMap.get(item), item, true);
            return true;
        }
        return false;
    }

    /**
     * get the subscribed items
     * @return the subscribed items
     */
    public List<T> getSubscribedItems() {
        return mSubscribedItems;
    }

    /**
     * replace subscribe items
     *
     * @param list the items to replace.
     */
    public void replaceSubscribeItems(List<T> list) {
        mSubscribedItems.clear();
        mSubscribedItems.addAll(list);
    }

    /**
     * the listener of on subscribe state change.
     *
     * @param <T> the item type.
     */
    public interface OnSubscribeStateChangeListener<T> {
        /**
         * @param view       the view
         * @param item       the item
         * @param subscribed if the item is subscribed.
         */
        void onSubscribeStateChange(View view, T item, boolean subscribed);
    }
}
