package com.heaven7.adapter.applier;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.RecycleViewComponentsAdapter;
import com.heaven7.adapter.util.ViewHelper2;

/**
 * use the space as margin between the items. and the item layout params must have margin params.
 * Created by heaven7 on 2019/3/5.
 * @since 2.0.0
 */
public class SpaceMarginApplier implements RecycleViewComponentsAdapter.AdapterApplier<ISelectable> {

    private final int space;

    public SpaceMarginApplier(int space) {
        this.space = space;
    }

    @Override
    public void apply(Context context, Object data,
                      int position, ISelectable item,
                      int itemLayoutId, ViewHelper2 helper) {
        View rootView = helper.getRootView();
        ViewGroup.LayoutParams lp = rootView.getLayoutParams();
        if(lp instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
            mlp.leftMargin = position != 0 ?  space  : 0;
        }else {
            System.err.println("SpaceMarginApplier >>> layout-params must be margin layout params.");
        }
    }
}
