package com.heaven7.adapter.applier;

import android.content.Context;
import android.view.View;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;
import com.heaven7.adapter.util.ViewHelper2;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public abstract class BaseViewApplier implements RecycleViewComponentsAdapter.AdapterApplier<ISelectable> {

    /** the view id. 0 mean use root of item. */
    private final int id;

    public BaseViewApplier(int viewId) {
        this.id = viewId;
    }
    public int getViewId() {
        return id;
    }

    @Override
    public void apply(Context context, Object contextData, int position, ISelectable item, int itemLayoutId, ViewHelper2 helper) {
        View view = id != 0 ? helper.getView(id) : helper.getRootView();
        apply(context, contextData, position, item, view);
    }

    protected abstract void apply(Context context, Object contextData, int position, ISelectable item, View view);
}
