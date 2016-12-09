package com.heaven7.android.mini.demo.extra;

import android.os.Bundle;
import android.view.View;

/**
 * Created by heaven7 on 2016/12/5.
 */
public class ViewPresenter implements IPresenter {

    private View mView;

    public ViewPresenter(View mView) {
        this.mView = mView;
    }

    @Override
    public void onStart(Bundle mArguments) {

    }

    @Override
    public void onStop(Bundle mArguments) {

    }

    @Override
    public void onDestroy(Bundle mArguments) {

    }
}
