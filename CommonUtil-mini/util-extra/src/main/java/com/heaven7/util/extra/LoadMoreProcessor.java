package com.heaven7.util.extra;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * implement RecyclerView.OnScrollListener. and handle load more.
 * you can get the callback with method {@link LoadMoreProcessor#onLoadMore(RecyclerView, int, int)}.
 * if you load more done , you should call {@link LoadMoreProcessor#setLoadingComplete()} to finish loading.
 * Created by heaven7 on 2015/10/10.
 */
public abstract class LoadMoreProcessor extends RecyclerView.OnScrollListener {

    private boolean mIsLoading;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int itemCount = recyclerView.getLayoutManager().getItemCount();
        if (dy > 0 && findLastVisibleItemPosition(recyclerView) == itemCount - 1) {
            //load more
            if (!mIsLoading) {
                mIsLoading = true;
                onLoadMore(recyclerView, dx, dy);
            }
        }
    }

    /**
     * return the last visible item position or -1 for unsupport LayoutManager.
     * @param rv the RecyclerView
     * @return the last visible item position or -1 for unsupport LayoutManager
     */
    public static int findLastVisibleItemPosition(RecyclerView rv) {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        int lastVisibleItemPosition = -1;
        if (lm instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) lm).findLastVisibleItemPosition();

        } else if (lm instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) lm).findLastVisibleItemPosition();

        } else if (lm instanceof StaggeredGridLayoutManager) {
            int positions[] =  ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(null);
            for(int pos : positions){
                if(pos > lastVisibleItemPosition){
                    lastVisibleItemPosition = pos;
                }
            }
        }
        return lastVisibleItemPosition;
    }


    /**
     *  called on load more
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx The amount of horizontal scroll.
     * @param dy The amount of vertical scroll.
     */
    protected abstract void onLoadMore(RecyclerView recyclerView, int dx, int dy);

    /**
     * set loading complete
     */
    public void setLoadingComplete() {
        mIsLoading = false;
    }

}
