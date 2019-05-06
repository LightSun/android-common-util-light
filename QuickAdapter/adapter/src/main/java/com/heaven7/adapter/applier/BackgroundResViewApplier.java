package com.heaven7.adapter.applier;

import android.content.Context;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public class BackgroundResViewApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.TwoStateApplier {

    private final int selectId;
    private final int unselectId;

    public BackgroundResViewApplier(int viewId, int selectId, int unselectId) {
        super(viewId);
        this.selectId = selectId;
        this.unselectId = unselectId;
    }

    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
        view.setBackgroundResource(item.isSelected() ? selectId : unselectId);
    }
}
