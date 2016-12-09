package com.heaven7.android.mini.demo.sample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.android.mini.demo.extra.PresenterLoader;
import com.heaven7.android.mini.demo.extra.PresenterMock;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.MainWorker;

/**
 * Test: mvp with loader. activity旋转时，不会调用loader的任何方法.destroy会自动调用loader.reset
 * Created by heaven7 on 2016/12/5.
 */
public class LoaderMvpTest extends BaseActivity {

    private static final String TAG = LoaderMvpTest.class.getSimpleName();
    private PresenterLoader<PresenterMock> mLoader;

    @Override
    protected int getlayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Logger.i(TAG, "onCreate");
        mLoader = new PresenterLoader<>(this, new PresenterMock());
        //自动调用startLoading
        getSupportLoaderManager().initLoader(0, new Bundle(), mLoaderCallback);

        MainWorker.postDelay(2000, new Runnable() {
            @Override
            public void run() {
                Logger.i(TAG, "called by postDelay method");
                // onLoadFinished 被调用的同时，loader也会调用
                mLoader.deliverResult(null);
            }
        });
        MainWorker.postDelay(5000, new Runnable() {
            @Override
            public void run() {
                Logger.i(TAG, "-------------- begin dispatch cancel");
                mLoader.deliverCancellation(); //此方法没有和 loaderManger交互
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       /* if (getLoaderManager().getLoader(0) == null) {
            getSupportLoaderManager().initLoader(0, null, mLoaderCallback);
        } else {
            getSupportLoaderManager().restartLoader(0, null, mLoaderCallback);
        }*/
    }

    @Override
    protected void onStart() {
        Logger.i(TAG, "onStart");
        super.onStart();  //自动调用 onStartLoading
    }

    @Override
    protected void onResume() {
        Logger.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.i(TAG, "onStop");
        super.onStop();  //自动调用 loader.onStopLoading.
    }

    @Override
    protected void onDestroy() {
        Logger.i(TAG, "onDestroy");
        super.onDestroy();  //不调用 destroyLoader。也会调用loader.reset方法
       // getSupportLoaderManager().destroyLoader(0);
    }

    private final LoaderManager.LoaderCallbacks<PresenterMock> mLoaderCallback = new LoaderManager.LoaderCallbacks<PresenterMock>() {

        @Override
        public Loader<PresenterMock> onCreateLoader(int id, Bundle args) {
            return mLoader;
        }

        @Override
        public void onLoadFinished(Loader<PresenterMock> loader, PresenterMock data) {
            Logger.i(TAG, "onLoadFinished");
        }

        @Override
        public void onLoaderReset(Loader<PresenterMock> loader) {
            Logger.i(TAG, "onLoaderReset");
        }
    };
}
