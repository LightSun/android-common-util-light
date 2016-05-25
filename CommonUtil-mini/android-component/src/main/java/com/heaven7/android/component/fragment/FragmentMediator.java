package com.heaven7.android.component.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.core.util.ViewHelper;

/**
 * Created by heaven7 on 2016/5/20.
 */
public final class FragmentMediator{

    private static final String PREFIX_KEY       = "COM.HEAVEN7.FRAGMENT.MEDIATOR";
    private static final String KEY_LAYOUT_ID    = PREFIX_KEY + "_layout_id";
    private static final String KEY_ARGUMENTS    = PREFIX_KEY + "_arguments";

    private final FragmentGroupCallbacks mCallback;
    private final FragmentCallbackContext mCallbackContext;

    /**
     * this bundle help to save and restore data. in this bundle, the data will automatic be saved and also restore to it.
     */
    private final Bundle mSaveBundle = new Bundle();
    private final Fragment mFragment;

    private ViewHelper mHelper;
    private MenuCallback mMenuCallback;
    private OnBindDataListener mBindDataListener;

    private int mLayoutId;
    private Bundle mArgs;

    FragmentMediator(Fragment fragment) {
        this.mFragment = fragment;
        this.mCallback = new FragmentGroupCallbacks();
        this.mCallbackContext = new FragmentCallbackContext();
        mCallbackContext.setFragmentMediator(this);

        this.mCallback.setFragmentCallbackContext(mCallbackContext);
    }

    /** called internal */
    void init(int layoutId, Bundle arguments){
        this.mLayoutId = layoutId;
        this.mArgs = arguments;
    }

    public Fragment getFragment(){
        return mFragment;
    }
    public ViewHelper getViewHelper(){
        return mHelper;
    }
    public OnBindDataListener getOnBindDataListener() {
        return mBindDataListener;
    }
    /*public*/ void setOnBindDataListener(OnBindDataListener l) {
        this.mBindDataListener = l;
    }

    /**
     * @return the bundle to help you save and restore, when data in this bundle, the data will automatic be saved.
     */
    public Bundle getSaveBundle(){
        return mSaveBundle;
    }

    public MenuCallback getMenuCallback() {
        return mMenuCallback;
    }

    public FragmentGroupCallbacks getFragmentCallbacks(){
        return mCallback;
    }

    /*public*/ void setMenuCallback(MenuCallback callback) {
        callback.setFragmentCallbackContext(mCallbackContext);
        this.mMenuCallback = callback;
    }

    /*public*/ void onAttach(Context context) {
        mCallback.onAttach(context);
    }

    /*public*/ void onCreate(Bundle savedInstanceState) {
        mCallback.onCreate(savedInstanceState);
    }

    /*public*/ View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        doRestoreState(savedInstanceState);
        return inflater.inflate(mLayoutId , container , false);
    }

    /*public*/ void onViewCreated(View view, Bundle savedInstanceState) {
        doRestoreState(savedInstanceState);
        mHelper = new ViewHelper(view);
        mBindDataListener.onBindData(mHelper , mArgs ,mSaveBundle);
    }

    /*public*/ void onActivityCreated(Bundle savedInstanceState) {
        doRestoreState(savedInstanceState);
        mCallback.onActivityCreated(mArgs , mSaveBundle);
    }

    /*public*/ void onDestroyView() {
        mCallback.onDestroyView();
    }

    /*public*/ void onDetach() {
        mCallback.onDetach();
    }

    /*public*/ void onStart() {
        mCallback.onStart();
    }

    /*public*/ void onResume() {
        mCallback.onResume();
    }

    /*public*/ void onPause() {
        mCallback.onPause();
    }

    /*public*/ void onStop() {
        mCallback.onStop();
    }

    /*public*/ void onDestroy() {
        mCallback.onDestroy();
    }

    //============================= menu ====================================

    /*public*/ void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mMenuCallback!=null) {
            mMenuCallback.onCreateOptionsMenu(menu, inflater);
        }
    }

    /*public*/ boolean onOptionsItemSelected(MenuItem item) {
        return mMenuCallback!=null && mMenuCallback.onOptionsItemSelected(item);
    }

    /*public*/ void onPrepareOptionsMenu(Menu menu) {
        if(mMenuCallback!=null) {
            mMenuCallback.onPrepareOptionsMenu(menu);
        }
    }

    /*public*/ void onOptionsMenuClosed(Menu menu) {
        if(mMenuCallback!=null) {
            mMenuCallback.onOptionsMenuClosed(menu);
        }
    }

    /*public*/ void onDestroyOptionsMenu() {
        if(mMenuCallback != null) {
            mMenuCallback.onDestroyOptionsMenu();
        }
    }

    /*public*/ void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(mMenuCallback == null || !mMenuCallback.onCreateContextMenu(menu, v, menuInfo)){
            getFragment().getActivity().onCreateContextMenu(menu, v, menuInfo);
        }
    }

    /*public*/ boolean onContextItemSelected(MenuItem item) {
        return mMenuCallback!=null && mMenuCallback.onContextItemSelected(item);
    }

    //============================================== end menu ==============================================//

    /*public*/ void onConfigurationChanged(Configuration newConfig) {
        mCallback.onConfigurationChanged(newConfig);
    }

    /*public*/ void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallback.onActivityResult(requestCode, resultCode, data);
    }

    /*public*/ void onLowMemory() {
        mCallback.onLowMemory();
    }

    /*public*/ void onSaveInstanceState(Bundle outState) {
        outState.putAll(mSaveBundle);
        outState.putInt(KEY_LAYOUT_ID,  mLayoutId );
        if(mArgs != null) {
            outState.putBundle(KEY_ARGUMENTS, mArgs);
        }
        mCallback.onSaveInstanceState(outState);
    }

    /*public*/ void onViewStateRestored(Bundle savedInstanceState) {
        doRestoreState(savedInstanceState);
        mCallback.onViewStateRestored(savedInstanceState);
    }

    private void doRestoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(KEY_LAYOUT_ID)){
                mLayoutId = savedInstanceState.getInt(KEY_LAYOUT_ID);
                savedInstanceState.remove(KEY_LAYOUT_ID);
            }
            if(savedInstanceState.containsKey(KEY_ARGUMENTS)){
                mArgs = savedInstanceState.getBundle(KEY_ARGUMENTS);
                savedInstanceState.remove(KEY_ARGUMENTS);
            }
            mSaveBundle.putAll(savedInstanceState);
        }
    }

    /*public*/ void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mCallback.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*public*/ void onSetUserVisibleHint(boolean isVisibleToUser) {
        mCallback.onSetUserVisibleHint(isVisibleToUser);
    }
}
