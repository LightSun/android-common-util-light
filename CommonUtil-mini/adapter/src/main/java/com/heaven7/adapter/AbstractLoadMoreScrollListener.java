package com.heaven7.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * implement RecyclerView.OnScrollListener. and handle load more.
 * Created by heaven7 on 2015/10/10.
 */
public abstract class AbstractLoadMoreScrollListener extends RecyclerView.OnScrollListener {

    protected int mLastVisiblePosition = RecyclerView.NO_POSITION;
    private boolean mLoading;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        int itemCount = recyclerView.getLayoutManager().getItemCount();
        if(newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisiblePosition!= RecyclerView.NO_POSITION
                && mLastVisiblePosition >= itemCount - 1){
            if(!mLoading) {
                mLoading = true;
                onLoadMore(recyclerView);
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        mLastVisiblePosition = RecyclerViewUtils.findLastVisibleItemPosition(recyclerView);
    }

    public void setLoadingComplete(){
        mLoading = false;
    }

    /**
     *  called on load more. often show footer.
     * @param rv The RecyclerView which scrolled.
     */
    protected abstract void onLoadMore(RecyclerView rv);


}
