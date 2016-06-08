package com.heaven7.memory.util;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;

/**
 * the runnable pool help get a runnable from the pool.
 * Created by heaven7 on 2016/6/7.
 */
public final class RunnablePool {
    private RunnablePool() {}
    private static Cacher<Runner,Void> sCacher;

    /** init the cacher ,this only can init once
     * @param  maxPoolSize the max pool size */
    public static void initCacher(int maxPoolSize){
        if(sCacher!=null)
            return ;
        sCacher = new Cacher<Runner,Void>(maxPoolSize) {
            @Override
            public Runner create(Void aa) {
                return new Runner() {
                    @Override
                    public void run() {
                        super.run();
                        afterRun();
                        sCacher.recycle(this);
                    }
                };
            }
        };
    }

    /**
     * obtain a Runner from the  pool. and the pool size is the default.
     * @param executor the really runnable execute
     * @param what what message to execute
     * @param params the params to execute
     * @return the Runner from cacher.
     */
    public static Runner obtain(IRunnbleExecutor executor, int what,Object...params){
        if(sCacher == null){
            initCacher(10);
        }
        final Runner runner = sCacher.obtain();
        runner.setExecutor(executor);
        runner.setWhat(what);
        runner.setParams(params);
        return runner;
    }

    /**
     * this is the runnable class help we reuse the runnable object.so it's high efficiency .
     * and after the {@link Runner#run()} is called. the Runner will atonmic be recycled to the cacher.
     */
   public static class Runner implements Runnable{

        private Object[] mParams;
        private IRunnbleExecutor mExecutor;
        private int what;
        private WeakReference<IRunnbleExecutor> mWeakExecutor;

        public void setParams(Object[] mParams) {
            this.mParams = mParams;
        }
        public void setExecutor(IRunnbleExecutor mExecutor) {
            if(mExecutor instanceof Fragment || mExecutor instanceof android.app.Fragment
                    || mExecutor instanceof Activity){
                this.mWeakExecutor = new WeakReference<>(mExecutor);
            }else {
                this.mExecutor = mExecutor;
            }
        }
        public int getWhat() {
            return what;
        }
        public void setWhat(int what) {
            this.what = what;
        }

        public Object[] getParams() {
            return mParams;
        }
        public IRunnbleExecutor getExecutor() {
            return mWeakExecutor!=null ? mWeakExecutor.get(): mExecutor;
        }

        @Override
        public void run() {
            final IRunnbleExecutor executor = getExecutor();
            if(executor == null){
                System.err.println("RunnablePool_Runner_run : executor == null or is recycled(Fragment or Activity)");
                return;
            }
            if(executor instanceof Activity){
                if(((Activity) executor).isFinishing()){
                    System.out.println("RunnablePool_Runner_run : executor is Activity and isFinishing() = true. ");
                    return;
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) executor).isDestroyed()){
                    System.out.println("RunnablePool_Runner_run : executor is Activity and isDestroyed() = true. ");
                    return;
                }
            }else if(executor instanceof Fragment){
                if(((Fragment) executor).isDetached() || ((Fragment) executor).isRemoving()){
                    // ((Fragment) executor).isVisible()
                    System.out.println("RunnablePool_Runner_run : executor is Fragment and isDestroyed() ||  isRemoving() = true. ");
                    return;
                }
            }else if(executor instanceof android.app.Fragment){
                if(((android.app.Fragment) executor).isDetached() || ((android.app.Fragment) executor).isRemoving()){
                    // ((Fragment) executor).isVisible()
                    System.out.println("RunnablePool_Runner_run : executor is android.app.Fragment and isDestroyed() ||  isRemoving() = true. ");
                    return;
                }
            }
            executor.execute(getWhat(), getParams());
        }

        protected void afterRun() {
            this.mWeakExecutor = null;
            this.mExecutor = null;
            this.mParams = null;
        }
    }

    /**
     * this is the runnable executor
     */
    public interface IRunnbleExecutor{

        /**
         *  execute the command impl
         */
        void execute(int what, Object... params);
    }
}
