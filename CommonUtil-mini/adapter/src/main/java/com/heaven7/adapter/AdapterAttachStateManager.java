package com.heaven7.adapter;

import android.support.v7.widget.RecyclerView;

/**
 *  a manager to manage the attach state of adapter
 */
public interface AdapterAttachStateManager {

    void addOnAttachStateChangeListener(OnAttachStateChangeListener l);

    void removeOnAttachStateChangeListener(OnAttachStateChangeListener l);

    boolean hasOnAttachStateChangeListener(OnAttachStateChangeListener l);

    interface OnAttachStateChangeListener{

        void onAttachedToRecyclerView(RecyclerView rv, RecyclerView.Adapter adapter);

        void onDetachedFromRecyclerView(RecyclerView rv, RecyclerView.Adapter adapter);
    }
}
