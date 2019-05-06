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
public class WidthHeightApplier extends BaseViewApplier implements RecycleViewComponentsAdapter.AdapterApplier<ISelectable> {

    private final SizeProvider provider;

    public WidthHeightApplier(int id, int width, int height) {
        this(id, new SimpleSizeProvider(width, height));
    }

    public WidthHeightApplier(int id, SizeProvider provider) {
        super(id);
        this.provider = provider;
    }

    @Override
    protected void apply(Context context, Object contextData, int position, ISelectable item, View view) {
        int viewId = getViewId();
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = provider.getWidth(context, contextData, position, item, viewId);
        lp.height = provider.getHeight(context, contextData, position, item, viewId);
    }

    public interface SizeProvider{
        int getWidth(Context context, Object contextData,
                     int position, ISelectable item, int viewId);
        int getHeight(Context context, Object contextData,
                      int position, ISelectable item, int viewId);
    }

    private static class SimpleSizeProvider implements SizeProvider{
        private final int width, height;
        public SimpleSizeProvider(int width, int height) {
            this.width = width;
            this.height = height;
        }
        @Override
        public int getWidth(Context context, Object contextData, int position, ISelectable item, int viewId) {
            return width;
        }
        @Override
        public int getHeight(Context context, Object contextData, int position, ISelectable item, int viewId) {
            return height;
        }
    }
}
