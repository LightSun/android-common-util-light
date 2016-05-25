package com.heaven7.android.component.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by heaven7 on 2016/5/20.
 */
public final class FragmentGroupCallbacks extends BaseFragmentCallback {

    private final ArrayList<IFragmentCallback> mCallbacks;

    /*public*/ FragmentGroupCallbacks() {
        this.mCallbacks = new ArrayList<>(5);
    }

    public void addFragmentCallback(IFragmentCallback callback){
        mCallbacks.add(callback);
    }

    public void addFragmentCallbacks(List<IFragmentCallback> callbacks){
        if(callbacks!=null) {
            for (IFragmentCallback callback : callbacks) {
                mCallbacks.add(callback);
            }
        }
    }
    public void addFragmentCallbacks(IFragmentCallback...callbacks){
        Collections.addAll(mCallbacks, callbacks);
    }
    public void removeFragmentCallback(IFragmentCallback callback){
        mCallbacks.remove(callback);
    }

    public void clearFragmentCallbacks(){
        mCallbacks.clear();
    }

    @Override
    public void onAttach(Context context) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onAttach(context);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState, Bundle saveBundle) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onActivityCreated(savedInstanceState, saveBundle);
        }
    }

  /*  @Override
    public void onViewCreated(ViewHelper view, Bundle arguments, Bundle saveBundle) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onViewCreated(view, arguments, saveBundle);
        }
    }*/

    @Override
    public void onDestroyView() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onDestroyView();
        }
    }

    @Override
    public void onDetach() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onDetach();
        }
    }

    @Override
    public void onStart() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onStart();
        }
    }

    @Override
    public void onResume() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onResume();
        }
    }

    @Override
    public void onPause() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onPause();
        }
    }

    @Override
    public void onStop() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLowMemory() {
        for( IFragmentCallback callback : mCallbacks){
            callback.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onViewStateRestored(savedInstanceState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onSetUserVisibleHint(boolean isVisibleToUser) {
        for( IFragmentCallback callback : mCallbacks){
            callback.onSetUserVisibleHint(isVisibleToUser);
        }
    }
}
