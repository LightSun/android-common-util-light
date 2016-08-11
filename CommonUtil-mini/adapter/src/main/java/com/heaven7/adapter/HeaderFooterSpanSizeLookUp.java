package com.heaven7.adapter;

import android.support.v7.widget.GridLayoutManager;

/**
 * for resolve one line in GridLayoutMainager
 * Created by heaven7 on 2016/8/10.
 */
public class HeaderFooterSpanSizeLookUp extends GridLayoutManager.SpanSizeLookup
        implements FullSpannableStaggeredGridLayoutManager.ISpanSizeLookupHelper{

    private final AdapterManager.IHeaderFooterManager mHFM;
    private int mSpanSize = 1;

    public HeaderFooterSpanSizeLookUp(AdapterManager.IHeaderFooterManager hfm, int spanSize) {
        if(spanSize <=0){
            throw new IllegalArgumentException("spanSize must >0");
        }
        this.mHFM = hfm;
        this.mSpanSize = spanSize;
    }

    public AdapterManager.IHeaderFooterManager getHeaderFooterManager(){
        return mHFM;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = mHFM.isHeader(position) || mHFM.isFooter(position);
        return isHeaderOrFooter ? mSpanSize : getSpanSizeImpl(position - mHFM.getHeaderSize()); //header / footer fill in
    }

    /** get the span size
     * @param position the position that without header and footer */
    protected int getSpanSizeImpl(int position) {
        return 1;
    }

    @Override
    public boolean shouldFullSpan(int position) {
        return mHFM.isHeader(position) || mHFM.isFooter(position);
    }
}
