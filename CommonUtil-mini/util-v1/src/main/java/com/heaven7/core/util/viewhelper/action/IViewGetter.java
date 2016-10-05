package com.heaven7.core.util.viewhelper.action;

import android.view.View;

import com.heaven7.core.util.ViewHelper;

/**
 * the view getter
 * Created by heaven7 on 2016/9/2.
 */
public interface IViewGetter<T extends View> {

    /**
     * called when got the view
     * @param view the view
     * @param vp the ViewHelper
     */
    void onGotView(T view , ViewHelper vp);
}
