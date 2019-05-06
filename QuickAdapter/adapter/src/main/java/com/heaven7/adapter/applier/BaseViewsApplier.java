package com.heaven7.adapter.applier;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;
import com.heaven7.adapter.util.ViewHelper2;

/**
 * the applier which apply the all view of item. but you can use {@linkplain #filter(View, Context, Object, int, ISelectable)}
 * to filter some views.
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
public abstract class BaseViewsApplier implements RecycleViewComponentsAdapter.AdapterApplier<ISelectable> {

    @Override
    public void apply(Context context, Object contextData, int position, ISelectable item, int itemLayoutId, ViewHelper2 helper) {
        applyViewInternal(context, contextData, position, item, helper.getRootView());
    }

    private void applyViewInternal(Context context, Object contextData, int position, ISelectable item, View view){
        if(!filter(view, context, contextData, position, item)){
            apply(view, context, contextData, position, item);
        }
        if(view instanceof ViewGroup){
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0 ; i < count ; i++){
                View child = ((ViewGroup) view).getChildAt(i);
                applyViewInternal(context, contextData, position, item, child);
            }
        }
    }

    protected boolean filter(View view, Context context, Object contextData, int position, ISelectable item){
        return false;
    }

    protected abstract void apply(View view, Context context, Object contextData, int position, ISelectable item);

}
