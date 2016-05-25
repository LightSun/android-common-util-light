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

    protected abstract void onCreateOptionsMenu(Menu menu, MenuInflater inflater);

    protected abstract boolean onOptionsItemSelected(MenuItem item);

    protected abstract void onPrepareOptionsMenu(Menu menu);

    protected abstract void onOptionsMenuClosed(Menu menu);

    protected abstract void onDestroyOptionsMenu();

    protected abstract boolean onContextItemSelected(MenuItem item);

    protected abstract boolean onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
}
