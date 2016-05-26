package com.heaven7.android.component.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.heaven7.core.util.BundleHelper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by heaven7 on 2016/5/20.
 */
public final class FragmentHelper {

    private final int mLayoutId;
    private final Bundle mArgs;
    private ArrayList<IFragmentCallback> mCallbacks;
    private MenuCallback mMenuCallback;
    private IDataBinder mDataBinder;

    public FragmentHelper(@LayoutRes int mLayoutId, Bundle mArgs) {
        this.mLayoutId = mLayoutId;
        this.mArgs = mArgs;
    }
    public FragmentHelper(@LayoutRes int mLayoutId, BundleHelper helper) {
        this(mLayoutId, helper.getBundle());
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public Bundle getArguments(){
        return mArgs;
    }

    public FragmentHelper addFragmentCallback(IFragmentCallback callback){
        if (mCallbacks==null){
            mCallbacks = new ArrayList<>(5);
        }
        this.mCallbacks.add(callback);
        return this;
    }
    public FragmentHelper addFragmentCallbacks(IFragmentCallback...callbacks){
        if (mCallbacks == null){
            mCallbacks = new ArrayList<>(5);
        }
        Collections.addAll(mCallbacks, callbacks);
        return this;
    }
    public ArrayList<IFragmentCallback> getFragmentCallbacks(){
        return mCallbacks;
    }

    public FragmentHelper setMenuCallback(MenuCallback callback){
        this.mMenuCallback = callback;
        return this;
    }

    public MenuCallback getMenuCallback(){
        return mMenuCallback;
    }

    public IDataBinder getDataBinder() {
        return mDataBinder;
    }

    public FragmentHelper setDataBinder(IDataBinder binder) {
        this.mDataBinder = binder;
        return this;
    }
}
