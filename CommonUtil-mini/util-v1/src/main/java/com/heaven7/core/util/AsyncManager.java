package com.heaven7.core.util;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * the async manager
 * Created by heaven7 on 2016/8/12.
 */
public abstract class AsyncManager{

    protected final MainHanlder mMainHandler;
    protected WorkHandler mWorkHandler;

    private Looper mLooper;
    private boolean mPrepared ;

    public AsyncManager() {
        mMainHandler = new MainHanlder(this);
        prepare();
    }

    public void prepare(){
        if(!mPrepared) {
            mPrepared = true;
            if (mLooper != null) {
                mLooper.quit();
            }
            HandlerThread ht = new HandlerThread("AsyncManager_" + getClass().getSimpleName());
            ht.start();
            mLooper = ht.getLooper();
            mWorkHandler = new WorkHandler(mLooper, this);
        }
    }
    public boolean isPrepared(){
        return mPrepared;
    }
    public void quit(){
        mPrepared = false;
        mLooper.quit();
    }

    /**
     *  process the message in work thread.this method is called in work thread(not in main thread). see {@link WorkHandler}
     * @param msg the message to handle.
     * @return true if handle success.
     */
    protected abstract boolean processMessageInWorkThread(Message msg);

    /**
     * handle the message in main thread.
     * @param msg the message
     * @return true if handle success.
     */
    protected abstract boolean processMessageInMainThread(Message msg);

    protected static class WorkHandler extends WeakHandler<AsyncManager> {
        public WorkHandler(Looper looper, AsyncManager apm) {
            super(looper, apm);
        }
        @Override
        public void handleMessage(Message msg) {
            get().processMessageInWorkThread(msg);
        }
    }

    protected static class MainHanlder extends WeakHandler<AsyncManager>{

        public MainHanlder(AsyncManager  apm) {
            super(Looper.getMainLooper(), apm);
        }
        @Override
        public void handleMessage(Message msg) {
            get().processMessageInMainThread(msg);
        }
    }

}
