package com.heaven7.adapter.applier;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public class BackgroundColorResViewApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.TwoStateApplier {

    private final int selectColorId;
    private final int unselectColorId;

    public BackgroundColorResViewApplier(int id, int selectColorId, int unselectColorId) {
        super(id);
        this.selectColorId = selectColorId;
        this.unselectColorId = unselectColorId;
    }

    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
        Resources res = context.getResources();
        view.setBackgroundColor(item.isSelected() ? res.getColor(selectColorId) : res.getColor(unselectColorId));
    }
}
