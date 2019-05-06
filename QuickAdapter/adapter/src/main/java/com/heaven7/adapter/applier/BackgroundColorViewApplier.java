package com.heaven7.adapter.applier;

import android.content.Context;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public class BackgroundColorViewApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.TwoStateApplier {

    private final int selectColor;
    private final int unselectColor;

    public BackgroundColorViewApplier(int id, int selectColor, int unselectColor) {
        super(id);
        this.selectColor = selectColor;
        this.unselectColor = unselectColor;
    }

    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
        view.setBackgroundColor(item.isSelected() ? selectColor : unselectColor);
    }
}
