package com.heaven7.core.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by heaven7 on 2016/10/27.
 */
public final class ViewUtil {

    public static void obtainFocus(View v){
        if(v!=null){
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.requestFocus();
            v.requestFocusFromTouch();
        }
    }

    /**
     * is the target view visible in local coordinate.
     * @param view the target view
     * @return true if visible.(visible means the view's all side can see).
     */
    public static boolean isVisibleInScreen(View view){
        if(view == null){
            throw new NullPointerException();
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return view.getLocalVisibleRect(new Rect(0, 0, dm.widthPixels, dm.heightPixels));
    }
}
