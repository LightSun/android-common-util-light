package com.heaven7.android.component.fragment;

import android.os.Bundle;

import com.heaven7.core.util.ViewHelper;

/**
 * Created by heaven7 on 2016/5/25.
 */
public interface OnBindDataListener {

    /**
     * called after the view of fragment is created.
     * @param view the viewHelper to control the all children view
     * @param arguments the args which is used to create fragment.
     * @param saveBundle the bundle to help you save state.
     */
    void onBindData(ViewHelper view, Bundle arguments, Bundle saveBundle);
}
