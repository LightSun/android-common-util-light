package com.heaven7.core.util.viewhelper.action;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.heaven7.core.util.ViewHelper;

/**
 * the background getter
 * Created by heaven7 on 2016/9/2.
 */
public abstract class AbstractBackgroundGetter<T extends Drawable> implements IViewGetter<View>{

    /**
     * called when  got the view background
     * @param t the background from view
     * @param view the view to get background
     * @param vp the ViewHelper
     */
    public abstract void onGotBackground(T  t, View view , ViewHelper vp);

    @Override
    public void onGotView(View view, ViewHelper vp) {
         onGotBackground((T) view.getBackground(), view ,vp);
    }
}
