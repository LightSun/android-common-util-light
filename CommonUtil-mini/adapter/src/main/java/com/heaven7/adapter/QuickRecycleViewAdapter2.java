package com.heaven7.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * a expand class of {@link QuickRecycleViewAdapter} that implements {@link AdapterAttachStateManager}.
 * Created by heaven7 on 2016/11/25.
 */
public abstract class QuickRecycleViewAdapter2<T extends ISelectable> extends QuickRecycleViewAdapter<T> implements AdapterAttachStateManager {

    private ArrayList<OnAttachStateChangeListener> mStateListeners;

    public QuickRecycleViewAdapter2(int layoutId, List<T> mDatas) {
        super(layoutId, mDatas);
    }
    public QuickRecycleViewAdapter2(int layoutId, List<T> mDatas, int selectMode) {
        super(layoutId, mDatas, selectMode);
    }

    @Override
    public void addOnAttachStateChangeListener(OnAttachStateChangeListener l) {
        if(mStateListeners == null){
            mStateListeners = new ArrayList<>();
        }
        mStateListeners.add(l);
    }
    @Override
    public void removeOnAttachStateChangeListener(OnAttachStateChangeListener l) {
        if(mStateListeners != null){
            mStateListeners.remove(l);
        }
    }
    @Override
    public boolean hasOnAttachStateChangeListener(OnAttachStateChangeListener l) {
        return mStateListeners != null && mStateListeners.contains(l);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(mStateListeners != null && mStateListeners.size() > 0) {
            for (OnAttachStateChangeListener l : mStateListeners) {
                l.onAttachedToRecyclerView(recyclerView, this);
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if(mStateListeners != null && mStateListeners.size() > 0) {
            for (OnAttachStateChangeListener l : mStateListeners) {
                l.onDetachedFromRecyclerView(recyclerView, this);
            }
        }
    }
}
