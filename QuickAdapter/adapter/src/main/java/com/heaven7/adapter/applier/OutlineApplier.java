package com.heaven7.adapter.applier;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;

/**
 * Created by heaven7 on 2019/3/11.
 * @since 2.0.0
 */
@TargetApi(21)
public class OutlineApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.AdapterApplier<ISelectable> {

    private final ViewOutlineProvider outlineProvider;

    public OutlineApplier(int resId, ViewOutlineProvider outlineProvider) {
        super(resId);
        this.outlineProvider = outlineProvider;
    }
    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
       if(Build.VERSION.SDK_INT >= 21){
           view.setOutlineProvider(outlineProvider);
           view.setClipToOutline(true);
       }
    }

}
