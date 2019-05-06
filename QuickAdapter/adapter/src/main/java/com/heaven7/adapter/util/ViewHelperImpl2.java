package com.heaven7.adapter.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heaven7.core.util.ViewCompatUtil;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.ViewHelperImpl;

/**
 * @author heaven7
 * @since 1.8.9
 */
public class ViewHelperImpl2 extends ViewHelperImpl {

    public ViewHelperImpl2(View target) {
        super(target);
    }
    public ViewHelperImpl2() {
    }
    private View v;

    /** change the current view to the target
     * @param target the target to view
     * @return  this */
    public ViewHelperImpl2 view(View target){
        if(target == null)
            throw new NullPointerException("target view can;t be null!");
        this.v = target;
        return this;
    }

    /**
     * reverse to the  t
     * @param  t  the object to reverse.
     * @param <T> the t
     * @return the t
     */
    public <T>T reverse(T t ){
        return t;
    }

    public Context getContext(){
        return v.getContext();
    }

    public ViewHelperImpl2 addTextChangedListener(TextWatcher watcher){
        ((TextView)v).addTextChangedListener(watcher);
        return this;
    }

    public ViewHelperImpl2 setVisibility(boolean visible){
        v.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
    public ViewHelperImpl2 setVisibility(int visibility){
        v.setVisibility(visibility);
        return this;
    }

    public ViewHelperImpl2 setText(CharSequence text){
        ((TextView)v).setText(text);
        return this;
    }

    public ViewHelperImpl2 setEnabled(boolean enable){
        v.setEnabled(enable);
        return this;
    }

    public ViewHelperImpl2 toogleVisibility(){
        View view = this.v;
        if(view.getVisibility() == View.VISIBLE){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }
    public ViewHelperImpl2 setImageURI(Uri uri) {
        ((ImageView)v).setImageURI(uri);
        return this;
    }
    public ViewHelperImpl2 setImageResource(int imageResId) {
        ((ImageView)v).setImageResource(imageResId);
        return this;
    }
    public ViewHelperImpl2 setBackgroundColor(int color) {
        v.setBackgroundColor(color);
        return this;
    }
    public ViewHelperImpl2 setBackgroundRes(int backgroundRes) {
        v.setBackgroundResource(backgroundRes);
        return this;
    }
    public ViewHelperImpl2 setBackgroundDrawable(Drawable d) {
        ViewCompatUtil.setBackgroundCompatible(v, d);
        return this;
    }
    public ViewHelperImpl setTextAppearance( int redId){
        ((TextView)v).setTextAppearance(v.getContext(), redId);
        return this;
    }
    public ViewHelperImpl2 setTextColor(int textColor) {
        ((TextView)v).setTextColor(textColor);
        return this;
    }
    public ViewHelperImpl2 setTextColor(ColorStateList colorList) {
        ((TextView)v).setTextColor(colorList);
        return this;
    }
    public ViewHelperImpl2 setTextColorRes(int textColorResId) {
        return setTextColor(getContext().getResources().getColor(textColorResId));
    }
    public ViewHelperImpl2 setTextColorStateListRes(int textColorStateListResId) {
        return setTextColor(getContext().getResources().getColorStateList(textColorStateListResId));
    }

    public ViewHelperImpl2 setImageDrawable(Drawable d) {
        ((ImageView)v).setImageDrawable(d);
        return this;
    }

    public ViewHelperImpl2 setImageUrl(String url,ViewHelper.IImageLoader loader) {
        loader.load(url, (ImageView) v);
        return this;
    }
    public ViewHelperImpl2 setImageBitmap(Bitmap bitmap) {
        ((ImageView)v).setImageBitmap(bitmap);
        return this;
    }
    public ViewHelperImpl2 setAlpha(float alpha) {
        ViewCompatUtil.setAlpha(v, alpha);
        return this;
    }
    public ViewHelperImpl2 linkify() {
        Linkify.addLinks((TextView) v, Linkify.ALL);
        return this;
    }
    /**
     *@see  Linkify#addLinks(TextView, int)
     * @param mask the mast
     * @return this
     */
    public ViewHelperImpl2 linkify(int mask) {
        Linkify.addLinks((TextView) v, mask);
        return this;
    }
    public ViewHelperImpl2 setTypeface(Typeface typeface) {
        TextView view = (TextView) this.v;
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }
    public ViewHelperImpl2 setProgress(int progress) {
        ((ProgressBar)v).setProgress(progress);
        return this;
    }
    public ViewHelperImpl2 setProgress(int progress, int max) {
        ((ProgressBar)v).setProgress(progress);
        ((ProgressBar)v).setMax(max);
        return this;
    }
    public ViewHelperImpl2 setProgressMax(int max) {
        ((ProgressBar)v).setMax(max);
        return this;
    }
    public ViewHelperImpl2 setRating(float rating) {
        ((RatingBar)v).setRating(rating);
        return this;
    }
    public ViewHelperImpl2 setRating(float rating, int max) {
        ((RatingBar)v).setRating(rating);
        ((RatingBar)v).setMax(max);
        return this;
    }
    public ViewHelperImpl2 setTag(Object tag) {
        v.setTag(tag);
        return this;
    }
    public ViewHelperImpl2 setTag(int key,Object tag) {
        v.setTag(key, tag);
        return this;
    }
    public ViewHelperImpl2 setChecked(boolean checked) {
        ((Checkable)v).setChecked(checked);
        return this;
    }
    //======================= listener =========================//

    public ViewHelperImpl2 setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener l) {
        ((CompoundButton)v).setOnCheckedChangeListener(l);
        return this;
    }
    public ViewHelperImpl2 setOnClickListener(View.OnClickListener l) {
        v.setOnClickListener(l);
        return this;
    }
    public ViewHelperImpl2 setOnLongClickListener(View.OnLongClickListener l) {
        v.setOnLongClickListener(l);
        return this;
    }
    public ViewHelperImpl2 setOnTouchListener(View.OnTouchListener l) {
        v.setOnTouchListener(l);
        return this;
    }
    public ViewHelperImpl2 setAdapter(Adapter adapter) {
        ((AdapterView)v).setAdapter(adapter);
        return this;
    }
    public ViewHelperImpl2 setRecyclerAdapter(RecyclerView.Adapter adapter) {
        ((RecyclerView)v).setAdapter(adapter);
        return this;
    }

    public ViewHelperImpl2 setEnable(boolean enable) {
        v.setEnabled(enable);
        return this;
    }

    public ViewHelperImpl2 setTextSizeDp(float size) {
        ((TextView)v).setTextSize(size);
        return this;
    }
    public ViewHelperImpl2 setTextSize(float size) {
        ((TextView)v).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }
    public ViewHelperImpl2 setScaleType(ImageView.ScaleType type) {
        ((ImageView)v).setScaleType(type);
        return this;
    }
    public ViewHelperImpl.GradientDrawableHelper beginGradientDrawableHelper() {
        GradientDrawable gd = (GradientDrawable) v.getBackground();
        return new ViewHelperImpl.GradientDrawableHelper(v.getContext(), gd);
    }

    //------------------------------------ new -------------------
    public ViewHelperImpl2 setImageTintColor(int color){
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)v).setImageTintList(ColorStateList.valueOf(color));
        }
        return this;
    }
    public ViewHelperImpl2 setImageTintList(@Nullable ColorStateList tint){
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)v).setImageTintList(tint);
        }
        return this;
    }
    public ViewHelperImpl2 setImageTintMode(PorterDuff.Mode mode){
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)v).setImageTintMode(mode);
        }
        return this;
    }

    public ViewHelperImpl2 setBackgroundTintColor(int color){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setBackgroundTintList(ColorStateList.valueOf(color));
        }
        return this;
    }
    public ViewHelperImpl2 setBackgroundTintList(@Nullable ColorStateList tint){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setBackgroundTintList(tint);
        }
        return this;
    }
    public ViewHelperImpl2 setBackgroundTintMode(PorterDuff.Mode mode){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setBackgroundTintMode(mode);
        }
        return this;
    }
    public ViewHelperImpl2 setForegroundTintList(@Nullable ColorStateList tint){
        if(Build.VERSION.SDK_INT >= 23) {
            v.setForegroundTintList(tint);
        }
        return this;
    }
    public ViewHelperImpl2 setForegroundTintMode(PorterDuff.Mode mode){
        if(Build.VERSION.SDK_INT >= 23) {
            v.setForegroundTintMode(mode);
        }
        return this;
    }
    public ViewHelperImpl2 setElevation(float evevation){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setElevation(evevation);
        }
        return this;
    }
    public ViewHelperImpl2 setClipToOutline(boolean clipToOutline){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setClipToOutline(clipToOutline);
        }
        return this;
    }
    public ViewHelperImpl2 setOutlineProvider(ViewOutlineProvider provider){
        if(Build.VERSION.SDK_INT >= 21) {
            v.setOutlineProvider(provider);
        }
        return this;
    }
   /* public ViewHelperImpl2 invalidateOutline(){
        if(Build.VERSION.SDK_INT >= 21) {
            v.invalidateOutline();
        }
        return this;
    }*/
}
