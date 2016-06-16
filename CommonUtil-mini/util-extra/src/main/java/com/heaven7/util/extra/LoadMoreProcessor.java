package com.heaven7.util.extra;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * default  support  RecyclerView with LinearLayoutManager
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
        if(dy > 0 && findLastVisibleItemPosition(recyclerView) == itemCount -1 ){
            //load more
            if( !mIsLoading ) {
                mIsLoading = true;
                onLoadMore(recyclerView,dx,dy);
            }
        }
    }

    protected int findLastVisibleItemPosition(RecyclerView recyclerView) {
        return ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
    }

    protected abstract void onLoadMore(RecyclerView recyclerView, int dx, int dy);

    public void setLoadingComplete(){
       mIsLoading = false;
    }

}
