package com.heaven7.adapter.drag;


import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.core.util.Logger;

/**
 * @author heaven7
 * @since 2.0.0
 */
//https://github.com/WangcWj/SideslippingDemo/blob/master/app/src/main/java/cn/example/wang/slideslipedemo/slideswaphelper/WItemTouchHelperPlus.java
public abstract class BaseItemTouchCallback extends WItemTouchHelperPlus.SimpleCallback {

    private static final String TAG = "BaseItemTouchCallback";
    private int mWidth;

    public BaseItemTouchCallback(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }
    @Override
    public String getItemSlideType() {
        //return WItemTouchHelperPlus.SLIDE_ITEM_TYPE_ITEMVIEW;
        return null;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    protected abstract int getSwipeViewId();
    protected abstract int[] getSwipeWidthViewIds();

    @Override
    public int getSwipeWidth(RecyclerView.ViewHolder holder) {
        if(mWidth == 0){
            int width = 0;
            for (int id : getSwipeWidthViewIds()){
                View viewById = holder.itemView.findViewById(id);
                width += viewById.getMeasuredWidth();
                if(viewById.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) viewById.getLayoutParams();
                    width += mlp.leftMargin + mlp.rightMargin;
                }
            }
            mWidth = width;
            for (int id : getSwipeWidthViewIds()){
                View viewById = holder.itemView.findViewById(id);
                viewById.setTranslationX(mWidth);
            }
        }
        return mWidth;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                @NonNull RecyclerView.ViewHolder target) {
        int pos = viewHolder.getAdapterPosition();
        int pos2 = target.getAdapterPosition();
        if(pos < 0 || pos2 < 0){
            return false;
        }
        RecyclerView.Adapter adapter1 = recyclerView.getAdapter();
        if(adapter1 instanceof ItemTouchAdapterDelegate){
            ((ItemTouchAdapterDelegate) adapter1).move(pos, pos2);
            return true;
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Logger.d(TAG, "onSwiped", "direction = " + direction);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        QuickRecycleViewAdapter<ISelectable> adapter = (QuickRecycleViewAdapter<ISelectable>) recyclerView.getAdapter();
        if(adapter.isHeader(pos) || adapter.isFooter(pos)){
            return;
        }

        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View view = viewHolder.itemView.findViewById(getSwipeViewId());
        int width = getSwipeWidth(viewHolder);
        if(dX < - width){
            dX = - width;
        }
        //flow the scroll
        for (int id : getSwipeWidthViewIds()){
            View viewById = viewHolder.itemView.findViewById(id);
            viewById.setTranslationX(dX + mWidth);
        }
        Logger.d(TAG, "onChildDraw", "dx = " + dX);
        view.setTranslationX(dX);
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //super.clearView(recyclerView, viewHolder);
        //recover the item view
        View view = viewHolder.itemView.findViewById(getSwipeViewId());
        if(view != null){
            view.setTranslationX(0);
        }
    }
}