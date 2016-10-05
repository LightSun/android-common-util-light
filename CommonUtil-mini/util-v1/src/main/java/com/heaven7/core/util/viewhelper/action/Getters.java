package com.heaven7.core.util.viewhelper.action;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * the all getters
 * Created by heaven7 on 2016/9/2.
 */
public final class Getters {

    public interface TextViewGetter  extends IViewGetter<TextView> {

    }
    public interface EditTextGetter  extends IViewGetter<EditText> {

    }
    public interface ImageViewGetter  extends IViewGetter<ImageView> {

    }
    public interface ButtonGetter  extends IViewGetter<Button> {

    }
    public interface RadioButtonGetter  extends IViewGetter<RadioButton> {

    }
    public interface ListViewGetter  extends IViewGetter<ListView> {

    }
    public interface GridViewGetter  extends IViewGetter<GridView> {

    }
    public interface RecyclerViewGetter  extends IViewGetter<RecyclerView> {

    }
    public static abstract class GradientDrawableGetter extends AbstractBackgroundGetter<GradientDrawable>{

    }

}
