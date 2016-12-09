package com.heaven7.android.mini.demo.extra;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by heaven7 on 2016/12/5.
 */
public class LoaderHelper {

    public static <P extends PresenterLoader.Callback> void initLoader(AppCompatActivity activity, P p, int id, Bundle args) {
       // activity.getSupportLoaderManager().initLoader(id, args, new LoaderCallbacksWrapper<>(activity, p, callbacks));
    }

    public static class LoaderCallbacksWrapper<P extends PresenterLoader.Callback> implements LoaderManager.LoaderCallbacks<P> {

        final P p;
        final Context context;

        public LoaderCallbacksWrapper(Context context, P p) {
            this.context = context;
            this.p = p;
        }

        @Override
        public Loader<P> onCreateLoader(int id, Bundle args) {
            return new PresenterLoader<>(context, p, args);
        }

        @Override
        public void onLoadFinished(Loader<P> loader, P data) {
            //Logger.i(TAG, "onLoadFinished");
           // data.onLoadFinished( ((PresenterLoader)loader).getArguments());
        }

        @Override
        public void onLoaderReset(Loader<P> loader) {
           // Logger.i(TAG, "onLoaderReset");
          //  p.onLoaderReset( ((PresenterLoader)loader).getArguments());
        }
    }
}
