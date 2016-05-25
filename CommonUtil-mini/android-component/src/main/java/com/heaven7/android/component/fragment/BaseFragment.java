package com.heaven7.android.component.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by heaven7 on 2016/5/20.
 */
public class BaseFragment extends Fragment{

    private final FragmentMediator mMediator = new FragmentMediator(this);

    public FragmentMediator getFragmentMediator(){
        return mMediator;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMediator.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediator.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Logger.i(getPageName(),"--- onCreateView ---");
        return mMediator.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMediator.onViewCreated(view,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMediator.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mMediator.onDestroyView();
        super.onDestroyView();
        // Logger.i(getPageName(),"--- onDestroyView ---");
    }

    @Override
    public void onDetach() {
        mMediator.onDetach();
        super.onDetach();
        //  Logger.i(getPageName(),"--- onDetach ---");
    }
    //===================================

    @Override
    public void onStart() {
        super.onStart();
        mMediator.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMediator.onResume();
        // Logger.i(getPageName(),"--- onResume ---");
    }
    @Override
    public void onPause() {
        super.onPause();
        mMediator.onPause();
        //  Logger.i(getPageName(),"--- onPause ---");
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediator.onStop();
        // Logger.i(getPageName(),"--- onStop ---");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediator.onDestroy();
        // Logger.i(getPageName(),"--- onDestroy ---");
    }

    //============================ menu ================
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        mMediator.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMediator.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
       // super.onPrepareOptionsMenu(menu);
        mMediator.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
       // super.onOptionsMenuClosed(menu);
        mMediator.onOptionsMenuClosed(menu);
    }

    @Override
    public void onDestroyOptionsMenu() {
        //super.onDestroyOptionsMenu();
        mMediator.onDestroyOptionsMenu();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        mMediator.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return mMediator.onContextItemSelected(item);
    }

    //============================== end =================================

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mMediator.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMediator.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMediator.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMediator.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mMediator.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mMediator.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mMediator.onSetUserVisibleHint(isVisibleToUser);
    }

}
