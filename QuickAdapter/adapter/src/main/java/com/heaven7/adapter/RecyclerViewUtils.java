package com.heaven7.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * the util class of RecyclerView
 * Created by heaven7 on 2016/8/11.
 */
public class RecyclerViewUtils {

    /**
     * return the last visible item position or -1 for unknown LayoutManager.
     * @param rv the RecyclerView
     * @return the last visible item position or -1 for unknown LayoutManager
     */
    public static int findLastVisibleItemPosition(RecyclerView rv) {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        int lastVisibleItemPosition = RecyclerView.NO_POSITION ;
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
     * create FullSpannableStaggeredGridLayoutManager and set ISpanSizeLookupHelper for full span of header/footer.
     * @param hfm the header/footer manager
     * @param spanCount the really span count of layoutManager
     * @param orientation the orientation of StaggeredGridLayoutManager.
     * @return a instance of FullSpannableStaggeredGridLayoutManager.
     */
    public static FullSpannableStaggeredGridLayoutManager createStaggeredGridLayoutManager(
            AdapterManager.IHeaderFooterManager hfm, int spanCount, int orientation){
        FullSpannableStaggeredGridLayoutManager lm = new FullSpannableStaggeredGridLayoutManager(spanCount,orientation);
        lm.setSpanSizeLookupHelper(new HeaderFooterSpanSizeLookUp(hfm,spanCount));
        return lm;
    }
    /**
     * create GridLayoutManager and set {@link android.support.v7.widget.GridLayoutManager.SpanSizeLookup} for full span of header/footer.
     * @param hfm the header/footer manager
     * @param spanCount the really span count of layoutManager
     * @param context the context.
     * @return a instance of GridLayoutManager.
     */
    public static GridLayoutManager createGridLayoutManager(AdapterManager.IHeaderFooterManager hfm,
                                                            Context context, int spanCount){
        GridLayoutManager lm = new GridLayoutManager(context,spanCount);
        lm.setSpanSizeLookup(new HeaderFooterSpanSizeLookUp(hfm,spanCount));
        return lm;
    }
}
