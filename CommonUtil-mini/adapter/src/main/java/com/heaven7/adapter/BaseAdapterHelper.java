/*
 * Copyright (C) 2015
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.core.util.ViewHelper;


/**
 * base adapter helper. write this based on Joan Zapata's 'base-adapter-helper'
 * and change something for easily use. use can see the source of his in github.
 * @author heaven7
 *
 */
/*public*/ class BaseAdapterHelper {

	public int   position;
	private int   layoutId;
	private ViewHelper mViewHelper;

	public BaseAdapterHelper(ViewGroup parent, int layoutId,int mPosition) {
		super();
		Context context = parent.getContext();
		this.position = mPosition;
		this.layoutId = layoutId;
		View root =  LayoutInflater.from(context).inflate(layoutId, parent, false);
		root.setTag(this);
		mViewHelper = new ViewHelper(root);
	}
	public BaseAdapterHelper(View item,int mPosition) {
		super();
		this.position = mPosition;
		item.setTag(this);
		mViewHelper = new ViewHelper(item);
	}

	public Context getContext(){
		return mViewHelper.getContext();
	}
	public int getPosition(){
		return position;
	}
	public int getLayoutId(){
		return layoutId;
	}

	/**
	 * @since 1.8.0
	 */
	public int getMenuLayoutId(){
		return 0;
	}

	public ViewHelper getViewHelper(){
		return mViewHelper;
	}

	// ================================================== //

	public static BaseAdapterHelper get(View convertView,
			ViewGroup parent, int layoutId){
		return get(convertView, parent, layoutId, -1);
	}
	
	public static BaseAdapterHelper get(View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new BaseAdapterHelper(parent, layoutId, position);
		}

		// Retrieve the existing helper and update its position
		BaseAdapterHelper existingHelper = (BaseAdapterHelper) convertView
				.getTag();

		if (existingHelper.layoutId != layoutId) {
			return new BaseAdapterHelper(parent, layoutId, position);
		}
		// RatingBar
		existingHelper.position = position;
		return existingHelper;
	}
	
}
