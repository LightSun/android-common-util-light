package com.heaven7.core.util.viewhelper.action;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.heaven7.core.util.ViewHelper;

/**
 * the background getter
 * Created by heaven7 on 2016/9/2.
 */
public abstract class AbstractBackgroundGetter<T extends Drawable> implements IViewGetter<View>{


    @Override
    public void onGotView(View view, ViewHelper vp) {
         onGotBackground((T) view.getBackground(), view ,vp);
    }

    /**
     * called when  got the view background
     * @param t the background from view
     * @param view the view to get background
     * @param vp the ViewHelper
     */
    protected void onGotBackground(T  t, View view , ViewHelper vp){
        onGotTextViewBackground(t, (TextView) view, vp);
    }

    /**
     * called when  got the TextView background
     * @param t the background from TextView
     * @param view the TextView to get background
     * @param vp the ViewHelper
     */
    protected void onGotTextViewBackground(T  t, TextView view , ViewHelper vp){

    }
}
