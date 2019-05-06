package com.heaven7.adapter.applier;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public class AlphaViewsApplier extends BaseViewsApplier implements RecycleViewComponentsAdapter.TwoStateApplier {

    private final float selectAlpha;
    private final float unselectAlpha;

    public AlphaViewsApplier(float selectAlpha, float unselectAlpha) {
        this.selectAlpha = selectAlpha;
        this.unselectAlpha = unselectAlpha;
    }

    @Override
    protected boolean filter(View view, Context context, Object contextData, int position, ISelectable item) {
        return view instanceof ViewGroup;
    }

    @Override
    protected void apply(View view, Context context, Object contextData, int position, ISelectable item) {
        view.setAlpha(item.isSelected() ? selectAlpha : unselectAlpha);
    }
}
