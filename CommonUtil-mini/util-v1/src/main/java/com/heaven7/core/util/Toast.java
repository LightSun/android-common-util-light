package com.heaven7.core.util;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * the common toast.
 * Created by heaven7 on 2017/5/8 0008.
 * @since 1.1.3
 */

public final class Toast {

    private static final int TOAST_SHOW_LENGTH = 2000; //toast展示的时间

    private final WindowManager mWM;
    private final WindowManager.LayoutParams mParams;
    private final Context mContext;

    private final Runnable mCancelRun = new Runnable() {
        @Override
        public void run() {
            cancel();
            if(mEnd != null){
                mEnd.run();
                mEnd = null;
            }
        }
    };
    private boolean mShowing;
    private View mToastView;
    private Runnable mStart;
    private Runnable mEnd;

    private Toast(Context context) {
        this.mContext = context;
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams = createDefault();
    }

    public static Toast create(Context context) {
        return new Toast(context);
    }

    /**
     * set a runnable run on toast start show.
     * @param action the show action
     * @return this
     */
    public Toast withStartAction(Runnable action) {
        this.mStart = action;
        return this;
    }
    /**
     * set a runnable run on toast end(without cancel outside).
     * @param action the end action
     * @return this
     */
    public Toast withEndAction(Runnable action) {
        this.mEnd = action;
        return this;
    }

    /**
     * assign the position of this toast.
     * @param x the x position
     * @param y the y position
     * @return this.
     */
    public Toast position(int x, int y){
        mParams.x = x;
        mParams.y = y;
        return this;
    }
    /**
     * assign the gravity of this toast.
     * see {@linkplain Gravity#CENTER} and etc.
     * @param gravity the gravity.
     * @return this.
     */
    public Toast gravity(int gravity){
        mParams.gravity = gravity;
        return this;
    }

    /**
     * enable or disable the click of this toast.
     * @param enable true to enable. false to disable.
     * @return this.
     */
    public Toast enableClick(boolean enable) {
        if (enable) {
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        } else {
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }
        return this;
    }

    /**
     * set the toast view to another view which is assigned by the layout.
     * @param layout the layout id.
     * @param parent the parent of layout. can be null.
     * @return this.
     */
    public Toast layout(@LayoutRes int layout, @Nullable ViewGroup parent) {
        mToastView = LayoutInflater.from(mContext).inflate(layout, parent);
        return this;
    }

    /**
     * perform bind the toast view which is assigned by {@linkplain #layout(int, ViewGroup)}.
     * @param binder the view binder.
     * @return this.
     */
    public Toast performViewBinder(IViewBinder binder) {
        if(mToastView == null){
            throw new NullPointerException();
        }
        binder.onBind(new ViewHelper(mToastView));
        return this;
    }

    /**
     * assign the animation style.
     * @param animStyle the anim style resource id
     * @return this.
     */
    public Toast animateStyle(@StyleRes int animStyle) {
        mParams.windowAnimations = animStyle;
        return this;
    }

    /**
     * show the toast.
     */
    public void show() {
        MainWorker.post(new Runnable() {
            @Override
            public void run() {
                showImpl();
            }
        });
    }

    private void showImpl() {
        if(mToastView == null){
            throw new NullPointerException();
        }
        cancel();
        mShowing = true;
        if(mStart != null){
            mStart.run();
            mStart = null;
        }
        //mToastView.setY(-mToastView.getMeasuredHeight());
        mWM.addView(mToastView, mParams);
        MainWorker.postDelay(TOAST_SHOW_LENGTH, mCancelRun);
    }

    /**
     * cancel the showing toast.
     */
    public void cancel() {
        MainWorker.remove(mCancelRun);
        if (mShowing) {
            mWM.removeView(mToastView);
            mShowing = false;
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    private WindowManager.LayoutParams createDefault() {
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = 0;
        //mParams.windowAnimations = R.style.topicAnim; //anim
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION; //toast need permission
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mParams.x = 0;
        mParams.y = 0;
        mParams.setTitle("toast");
        // mParams.token = ((Activity)context)
        return mParams;
    }

    /**
     * the view binder
     */
    public interface IViewBinder {
        /**
         * called on bind view
         * @param helper the view helper.
         */
        void onBind(ViewHelper helper);
    }

}
