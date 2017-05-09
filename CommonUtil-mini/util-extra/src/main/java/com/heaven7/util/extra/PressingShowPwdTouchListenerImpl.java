package com.heaven7.util.extra;

import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * long press drawable left or right to show password.
 * <h1>  Note:
 * only used for {@linkplain EditText} and must have drawable Right or left.</h1>
 * Created by heaven7 on 2017/4/19 0019.
 */

public class PressingShowPwdTouchListenerImpl implements View.OnTouchListener{

    /** drawable in left or right */
    private final boolean mDrawableRight;

    public PressingShowPwdTouchListenerImpl() {
       this(true);
    }
    public PressingShowPwdTouchListenerImpl(boolean drawableRight) {
        this.mDrawableRight = drawableRight;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(! (v instanceof EditText)){
            return false;
        }
        final EditText et = (EditText) v;
        final int drawablePadding = et.getCompoundDrawablePadding();
        final Drawable drawable = mDrawableRight ? et.getCompoundDrawables()[2]
                : et.getCompoundDrawables()[0];
        //如果右边没有图片，不再处理
        if (drawable == null)
            return false;
        //按压图片显示明文
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            boolean showPwd = false;
            if(mDrawableRight) {
                if (event.getX() > et.getWidth()
                        - et.getPaddingRight()
                        - drawable.getIntrinsicWidth() - drawablePadding) {
                    showPwd = true;
                }
            }else {
                if (event.getX() > et.getPaddingLeft() && event.getX() <
                        et.getPaddingLeft() + drawable.getIntrinsicWidth() + drawablePadding) {
                    //left drawable.
                    showPwd = true;
                }
            }
            if(showPwd) {
                //显示密码明文
                et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                et.postInvalidate();
                CharSequence charSequence = et.getText();
                //为了保证体验效果，需要保持输入焦点在文本最后一位
                if (charSequence != null) {
                    et.setSelection(charSequence.length());
                }
            }
        }else if (event.getAction() == MotionEvent.ACTION_UP){
            //隐藏密码明文
            et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            et.postInvalidate();
            CharSequence charSequence = et.getText();
            if(charSequence != null) {
                et.setSelection(charSequence.length());
            }
        }
        return false;
    }
}
