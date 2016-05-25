package com.heaven7.android.component.fragment;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by heaven7 on 2016/5/24.
 */
public abstract class MenuCallback extends BaseFragmentCallback0 {

    protected  void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    protected  boolean onOptionsItemSelected(MenuItem item){
         return false;
    }

    protected  void onPrepareOptionsMenu(Menu menu){

    }

    protected  void onOptionsMenuClosed(Menu menu){

    }

    protected  void onDestroyOptionsMenu(){

    }

    protected  boolean onContextItemSelected(MenuItem item){
        return false;
    }

    protected  boolean onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        return false;
    }
}
