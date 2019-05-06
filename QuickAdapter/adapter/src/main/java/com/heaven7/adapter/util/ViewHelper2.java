package com.heaven7.adapter.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.viewhelper.action.IViewGetter;

/**
 * @author heaven7
 * @since 1.8.9
 */
public class ViewHelper2 extends ViewHelper {

	private SparseArray<View> mViewMap;
	private View mRootView;
	private ViewHelperImpl2 mImpl;
	private LayoutInflater mInflater;
	
	public ViewHelper2(View root){
		super(root);
		this.mRootView = root;
		mInflater = LayoutInflater.from(root.getContext());
		mViewMap = new SparseArray<View>();
		mImpl = new ViewHelperImpl2(null);
	}

	public ViewHelper2 setRootOnClickListener(View.OnClickListener l){
		mRootView.setOnClickListener(l);
		return this;
	}
	public void clearCache(){
		mViewMap.clear();
	}
	public View getRootView() {
		return mRootView;
	}
	public Context getContext(){
		return mRootView.getContext();
	}
	public Resources getResources(){
		return getContext().getResources();
	}
	public LayoutInflater getLayoutInflater(){
		return mInflater;
	}

	public ViewHelper2 addTextChangedListener(int viewId ,TextWatcher watcher){
		return view(viewId).addTextChangedListener(watcher).reverse(this);
	}
	public ViewHelper2 setTextSizeDp(int viewId ,float size){
		return view(viewId).setTextSizeDp(size).reverse(this);
	}
	public ViewHelper2 setTextSize(int viewId ,float size){
		return view(viewId).setTextSize(size).reverse(this);
	}

	public ViewHelper2 setText(int viewId,CharSequence text){
		return view(viewId).setText(text).reverse(this);
	}

	public ViewHelper2 setEnable(int viewId, boolean enable) {
		return view(viewId).setEnable(enable).reverse(this);
	}
	/**
	 * toogle the visibility of the view.such as: VISIBLE to gone or gone to VISIBLE
	 * @param viewId  the id of view
	 *                @return this
	 */
	public ViewHelper2 toogleVisibility(int viewId){
		return view(viewId).toogleVisibility().reverse(this);
	}

	/** get the view in current layout . return null if the viewid can't find in current layout
	 * @param viewId the id of view
	 *  @param <T> the any view
	 * @return  the view
	 *  */
	@SuppressWarnings("unchecked")
	public <T extends View > T getView(int viewId) {
		View view = mViewMap.get(viewId);
		if(view == null){
			view = mRootView.findViewById(viewId);
			if(view ==null)
				throw new IllegalStateException("can't find the view ,id = " +viewId);
			mViewMap.put(viewId, view);
		}
		return (T) view;
	}
	/**
	 * Will set the image of an ImageView from a resource id.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageResId
	 *            The image resource id.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setImageResource(int viewId, int imageResId) {
		return  view(viewId).setImageResource(imageResId).reverse(this);
	}
	/**
	 * Will set background color of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param color
	 *            A color, not a resource id.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setBackgroundColor(int viewId, int color) {
		return view(viewId).setBackgroundColor(color).reverse(this);
	}

	/**
	 * Will set background of a view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param backgroundRes
	 *            A resource to use as a background. 0 to remove it.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setBackgroundRes(int viewId, int backgroundRes) {
		return view(viewId).setBackgroundRes(backgroundRes).reverse(this);
	}
	public ViewHelper2 setBackgroundDrawable(int viewId,Drawable drawable){
		return view(viewId).setBackgroundDrawable(drawable).reverse(this);
	}
	/**
	 * Will set text color of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param textColor
	 *            The text color (not a resource id).
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setTextColor(int viewId, int textColor) {
		return view(viewId).setTextColor(textColor).reverse(this);
	}

	/**
	 * Sets the text color, size, style, hint color, and highlight color
	 * from the specified TextAppearance resource.
	 * @param viewId the id of view
	 * @param  resId the id of TextAppearance resource
	 * @return this
	 */
	public ViewHelper2 setTextAppearance(int viewId, int resId) {
		return view(viewId).setTextAppearance(resId).reverse(this);
	}
	/**
	 * Will set text color of a TextView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param textColorRes
	 *            The text color resource id.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setTextColorRes(int viewId, int textColorRes) {
		return view(viewId).setTextColorRes(textColorRes).reverse(this);
	}
	
	/**
	 * Will set the image of an ImageView from a drawable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param drawable
	 *            The image drawable.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setImageDrawable(int viewId, Drawable drawable) {
		return view(viewId).setImageDrawable(drawable).reverse(this);
	}

	/**
	 * Will download an image from a URL and put it in an ImageView.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param imageUrl
	 *            The image URL.
	 * @param loader 
	 *             which to load image actually.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setImageUrl(int viewId, String imageUrl,IImageLoader loader) {
		return view(viewId).setImageUrl(imageUrl, loader).reverse(this);
	}
	/**set the image uri
	 * @since 1.0.2
	 * @param viewId the id of view
	 * @param uri the uri
	 * @return this
	 * */
	public ViewHelper2 setImageURI(int viewId, Uri uri) {
		return view(viewId).setImageURI(uri).reverse(this);
	}
	/**set the image scale type
	 * @since 1.0.2
	 * @param viewId the id of view
	 * @param type the scale type
	 * @return  this
	 * */
	public ViewHelper2 setScaleType(int viewId, ImageView.ScaleType type) {
		return view(viewId).setScaleType(type).reverse(this);
	}
	/**
	 * Add an action to set the image of an image view. Can be called multiple
	 * times.
	 * @param viewId this id of viewId
	 * @param bitmap the bit map
	 * @return this
	 */
	public ViewHelper2 setImageBitmap(int viewId, Bitmap bitmap) {
		return view(viewId).setImageBitmap(bitmap).reverse(this);
	}

	/**
	 * Add an action to set the alpha of a view. Can be called multiple times.
	 * Alpha between 0-1.
	 * @param viewId the id of view
	 * @param value the alpha value
	 * @return this
	 */
	public ViewHelper2 setAlpha(int viewId, float value) {
		return view(viewId).setAlpha(value).reverse(this);
	}
	

	/**
	 * Set a view visibility to VISIBLE (true) or GONE (false).
	 * 
	 * @param viewId
	 *            The view id.
	 * @param visible
	 *            True for VISIBLE, false for GONE.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setVisibility(int viewId, boolean visible) {
		return view(viewId).setVisibility(visible).reverse(this);
	}
	public ViewHelper2 setVisibility(int viewId, int visibility) {
		return view(viewId).setVisibility(visibility).reverse(this);
	}

	
	/**
	 * Add links into a TextView. default is 
	 * 
	 * @param viewId
	 *            The id of the TextView to linkify.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 linkify(int viewId) {
		return view(viewId).linkify().reverse(this);
	}
	private <T extends View> T retrieveView(int viewId) {
		return getView(viewId);
	}

	/** Add links into a TextView,
	 * @param  linkifyMask ,see {@link Linkify#ALL} and etc.
	 * @param viewId the if of view
	 * @return this*/
	public ViewHelper2 linkify(int viewId,int linkifyMask) {
		return view(viewId).linkify(linkifyMask).reverse(this);
	}

	/** Apply the typeface to the given viewId, and enable subpixel rendering.
	 * @param typeface the typeface
	 * @param viewId the ids of view
	 * @return this */
	public ViewHelper2 setTypeface(int viewId, Typeface typeface) {
		return view(viewId).setTypeface(typeface).reverse(this);
	}

	/**
	 * Apply the typeface to all the given viewIds, and enable subpixel
	 * rendering.
	 * @param typeface the typeface
	 * @param viewIds the ids of views
	 * @return this
	 */
	public ViewHelper2 setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			setTypeface(viewId,typeface);
		}
		return this;
	}
	
	/**
	 * Sets the progress of a ProgressBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param progress
	 *            The progress.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setProgress(int viewId, int progress) {
		return view(viewId).setProgress(progress).reverse(this);
	}

	/**
	 * Sets the progress and max of a ProgressBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param progress
	 *            The progress.
	 * @param max
	 *            The max value of a ProgressBar.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setProgress(int viewId, int progress, int max) {
		return view(viewId).setProgress(progress,max).reverse(this);
	}

	/**
	 * Sets the range of a ProgressBar to 0...max.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param max
	 *            The max value of a ProgressBar.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setProgressMax(int viewId, int max) {
		return view(viewId).setProgressMax(max).reverse(this);
	}

	/**
	 * Sets the rating (the number of stars filled) of a RatingBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param rating
	 *            The rating.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setRating(int viewId, float rating) {
		return view(viewId).setRating(rating).reverse(this);
	}

	/**
	 * Sets the rating (the number of stars filled) and max of a RatingBar.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param rating
	 *            The rating.
	 * @param max
	 *            The range of the RatingBar to 0...max.
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setRating(int viewId, float rating, int max) {
		return view(viewId).setRating(rating,max).reverse(this);
	}

	/**
	 * Sets the tag of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param tag
	 *            The tag;
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setTag(int viewId, Object tag) {
		return view(viewId).setTag(tag).reverse(this);
	}

	/**
	 * Sets the tag of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param key
	 *            The value of tag;
	 * @param tag
	 *            The tag;
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setTag(int viewId, int key, Object tag) {
		return view(viewId).setTag(key,tag).reverse(this);
	}

	/**
	 * Sets the checked status of a checkable.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param checked
	 *            The checked status;
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setChecked(int viewId, boolean checked) {
		return view(viewId).setChecked(checked).reverse(this);
	}
	
	/** set OnCheckedChangeListener to CompoundButton or it's children.
	 * @param viewId the id of view
	 * @param l the OnCheckedChangeListener
	 * @return  this */
	public ViewHelper2 setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener l){
		return view(viewId).setOnCheckedChangeListener(l).reverse(this);
	}

	/**
	 * Sets the adapter of a adapter view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param adapter
	 *            The adapter;
	 * @return The ViewHelper2 for chaining.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ViewHelper2 setAdapter(int viewId, Adapter adapter) {
		return view(viewId).setAdapter(adapter).reverse(this);
	}

	/**
	 * Sets the on click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on click listener;
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setOnClickListener(int viewId,
			View.OnClickListener listener) {
		return view(viewId).setOnClickListener(listener).reverse(this);
	}

	/**
	 * Sets the on touch listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on touch listener;
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setOnTouchListener(int viewId,
			View.OnTouchListener listener) {
		return view(viewId).setOnTouchListener(listener).reverse(this);
	}

	/**
	 * Sets the on long click listener of the view.
	 * 
	 * @param viewId
	 *            The view id.
	 * @param listener
	 *            The on long click listener;
	 * @return The ViewHelper2 for chaining.
	 */
	public ViewHelper2 setOnLongClickListener(int viewId,
			View.OnLongClickListener listener) {
		return view(viewId).setOnLongClickListener(listener).reverse(this);
	}

	/***
	 *  view the target,after call this you can call multi setXXX methods on the target view.
	 *  such as: <pre>
	 *      ViewHelper2.view(xx).setText("sss").setOnclickListener(new OnClickListener()...);
	 *  </pre>
	 * @param viewId the id of target view
	 * @return  ViewHelper2Impl
	 */
	public ViewHelperImpl2 view(int viewId){
		return mImpl.view(getView(viewId));
	}

	/** view the root view
	 * @since 1.0.2
	 * @return ViewHelper2Impl
	 * */
	public ViewHelperImpl2 viewRoot(){
		return mImpl.view(getRootView());
	}

	@SuppressWarnings("unchecked")
	public <T extends View> ViewHelper2 performViewGetter(int viewId, IViewGetter<T> getter){
		getter.onGotView((T) getView(viewId), this);
		return this;
	}
	//-------------------------- new 1.8.9 -----------------------------------

	public ViewHelper2 setImageTintColor(int color){
		return mImpl.setImageTintColor(color).reverse(this);
	}
	public ViewHelper2 setImageTintList(@Nullable ColorStateList tint){
		return mImpl.setImageTintList(tint).reverse(this);
	}
	public ViewHelper2 setImageTintMode(PorterDuff.Mode mode){
		return mImpl.setImageTintMode(mode).reverse(this);
	}
	public ViewHelper2 setBackgroundTintColor(int color){
		return mImpl.setBackgroundTintColor(color).reverse(this);
	}
	public ViewHelper2 setBackgroundTintList(@Nullable ColorStateList tint){
		return mImpl.setBackgroundTintList(tint).reverse(this);
	}
	public ViewHelper2 setBackgroundTintMode(PorterDuff.Mode mode){
		return mImpl.setBackgroundTintMode(mode).reverse(this);
	}
	public ViewHelper2 setForegroundTintList(@Nullable ColorStateList tint){
		return mImpl.setForegroundTintList(tint).reverse(this);
	}
	public ViewHelper2 setForegroundTintMode(PorterDuff.Mode mode){
		return mImpl.setForegroundTintMode(mode).reverse(this);
	}
	public ViewHelper2 setElevation(float evevation){
		return mImpl.setElevation(evevation).reverse(this);
	}
	public ViewHelper2 setClipToOutline(boolean clipToOutline){
		return mImpl.setClipToOutline(clipToOutline).reverse(this);
	}
	public ViewHelper2 setOutlineProvider(ViewOutlineProvider provider){
		return mImpl.setOutlineProvider(provider).reverse(this);
	}

}