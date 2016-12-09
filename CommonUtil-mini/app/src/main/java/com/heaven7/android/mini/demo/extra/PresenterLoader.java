package com.heaven7.android.mini.demo.extra;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.heaven7.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * the presenter loader
 * Created by heaven7 on 2016/12/5.
 */
public class PresenterLoader<P extends PresenterLoader.Callback> extends Loader<P> {

    private static final String TAG = "PresenterLoader";
   // private final P  mPresenter;
    private final Bundle mArguments;
    private final List<Callback> mCallbacks = new ArrayList<>();

    public PresenterLoader(Context context, P presenter) {
       this(context, presenter , null);
    }
    public PresenterLoader(Context context, P presenter, Bundle args) {
        super(context);
        //this.mPresenter = presenter;
        this.mArguments = args;
        mCallbacks.add(presenter);
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Logger.i(TAG, "onStartLoading");
        dispatchOnStart();
        //deliverResult(mPresenter);
    }

    private void dispatchOnStart() {
        for (Callback callback : mCallbacks){
            callback.onStart(mArguments);
        }
    }
    private void dispatchOnStop() {
        for (Callback callback : mCallbacks){
            callback.onStop(mArguments);
        }
    }
    private void dispatchOnDestroy() {
        for (Callback callback : mCallbacks){
            callback.onDestroy(mArguments);
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Logger.i(TAG, "onStopLoading");
        dispatchOnStop();
    }

    @Override
    protected boolean onCancelLoad() {
        Logger.i(TAG, "onCancelLoad");
        return super.onCancelLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Logger.i(TAG, "onForceLoad");
    }

    @Override
    protected void onReset() {
        super.onReset();
        Logger.i(TAG, "onReset");
        dispatchOnDestroy();
    }

    public abstract static class Callback{

        public abstract  void onDestroy(Bundle mArguments);

        public abstract  void onStart(Bundle mArguments);

        public void onStop(Bundle mArguments){
        }
    }

  /*  public void deliverResult(P p){
        super.deliverResult(p);
    }*/

}
