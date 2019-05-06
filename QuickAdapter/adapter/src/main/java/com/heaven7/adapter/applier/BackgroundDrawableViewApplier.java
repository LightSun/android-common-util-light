package com.heaven7.adapter.applier;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public class BackgroundDrawableViewApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.TwoStateApplier {

    private Drawable select;
    private Drawable unselect;

    public BackgroundDrawableViewApplier(int viewId, Drawable select, Drawable unselect) {
        super(viewId);
        this.select = select;
        this.unselect = unselect;
    }
    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
        view.setBackground( item.isSelected() ? select : unselect);
    }
}
