package com.heaven7.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * this class help we handle if full span with item.
 */
public class FullSpannableStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    private final String TAG = getClass().getSimpleName();

    private ISpanSizeLookupHelper mSpanSizeLookup;

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". Defaults to single column and vertical.
     * @param context the context
     * @param attrs the attrs
     * @param defStyleAttr the default style attr
     * @param defStyleRes the fefault style res
     */
    public FullSpannableStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                      int defStyleRes) {
        super(context,attrs,defStyleAttr,defStyleRes);
    }

    public FullSpannableStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public ISpanSizeLookupHelper getSpanSizeLookupHelper() {
        return mSpanSizeLookup;
    }

    /**
     * set span size lookup
     * @param spanSizeLookup the SpanSizeLookup Helper
     */
    public void setSpanSizeLookupHelper(ISpanSizeLookupHelper spanSizeLookup) {
        mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        //Log.d(TAG, "item count = " + getItemCount());
        final ISpanSizeLookupHelper mSpanSizeLookup = this.mSpanSizeLookup;
        if(mSpanSizeLookup!=null) {
            for (int i = 0; i < getItemCount(); i++) {
                if (mSpanSizeLookup.shouldFullSpan(i)) {
                    //Log.d(TAG, "lookup > 1 = " + i);
                    try {
                        //fix dynamic add IndexOutOfBoundsException
                        View view = recycler.getViewForPosition(i);
                        if (view != null) {
                            /**
                             * hold all column
                             * @see https://plus.google.com/+EtienneLawlor/posts/c5T7fu9ujqi
                             */
                            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                            lp.setFullSpan(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onMeasure(recycler, state, widthSpec, heightSpec);
    }

    /**
     * this interface help we handle full span about {@link android.support.v7.widget.GridLayoutManager.SpanSizeLookup}
     */
    public interface ISpanSizeLookupHelper{
        /**
         * indicate the target position should be full span or not.
         * @param position the target position
         * @return true if the target position should be full span
         */
         boolean  shouldFullSpan(int position);
    }
}
