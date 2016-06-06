package com.heaven7.util.extra;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

public class SelectorUtil extends View{
	
	private SelectorUtil(Context context) {
		super(context);
	}

	/**
	 * 得到按钮点击效果
	 * @param context 上下文对象
	 * @param normalResId 默认时图片的资源id
	 * @param selectedResId 选中时图片的资源id
	 * @param pressedResId 按下时图片的资源id
	 * @return StateListDrawable
	 */
	public static StateListDrawable getSelectorDrawable(Context context,int normalResId,int selectedResId,int pressedResId) {
		if(context == null){
			return null;
		}
        StateListDrawable bgDrawable = new StateListDrawable();
        Resources resources = context.getResources();
        Drawable normal = resources.getDrawable(normalResId);
        Drawable selected = resources.getDrawable(selectedResId);
        Drawable pressed = resources.getDrawable(pressedResId);
        bgDrawable.addState(View.PRESSED_ENABLED_STATE_SET, pressed);
        bgDrawable.addState(View.ENABLED_FOCUSED_STATE_SET, selected);
        bgDrawable.addState(View.FOCUSED_STATE_SET, selected);
        bgDrawable.addState(View.ENABLED_STATE_SET, normal);
        bgDrawable.addState(View.EMPTY_STATE_SET, normal);
        return bgDrawable;
    }
	
	public static ColorStateList getSelectorColor(Context context,int normalColorId,int selectedColorId,int pressedColorId) {
		if(context == null){
			return null;
		}
		int[][] states = new int[5][];
		int[] colors = new int[5];
		states[0] = View.PRESSED_ENABLED_STATE_SET;
		states[1] = View.PRESSED_ENABLED_STATE_SET;
		states[2] = View.FOCUSED_STATE_SET;
		states[3] = View.ENABLED_STATE_SET;
		states[4] = View.EMPTY_STATE_SET;
		colors[0] = pressedColorId;
		colors[1] = selectedColorId;
		colors[2] = selectedColorId;
		colors[3] = normalColorId;
		colors[4] = normalColorId;
		ColorStateList bgColor = new ColorStateList(states, colors);
        return bgColor;
    }
	
}
