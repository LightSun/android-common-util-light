package com.heaven7.android.component.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.heaven7.core.util.ViewHelper;

/**
 * the all callback method is from fragment.
 * Created by heaven7 on 2016/5/20.
 */
public interface IFragmentCallback {

    void onAttach(Context context);

    void onCreate(Bundle savedInstanceState);

   // View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * @param view the viewHelper to control the all children view
     * @param arguments the args which is used to create fragment.
     * @param saveBundle the bundle to help you save state.
     */
    void onViewCreated(ViewHelper view, Bundle arguments, Bundle saveBundle);

    /**
     * @param arguments the args which is used to create fragment.
     * @param saveBundle the bundle to help you save state.
     */
    void onActivityCreated(Bundle arguments, Bundle saveBundle);

    void onDestroyView();

    void onDetach();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();


    void onConfigurationChanged(Configuration newConfig);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onLowMemory();

    void onSaveInstanceState(Bundle outState);

    void onViewStateRestored(Bundle savedInstanceState);

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    void onSetUserVisibleHint(boolean isVisibleToUser);
}
