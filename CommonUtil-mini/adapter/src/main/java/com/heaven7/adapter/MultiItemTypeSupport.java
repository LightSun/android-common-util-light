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

import android.support.annotation.LayoutRes;

/**
 * used to multi item support. such as: Listview contains two items
 * @author heaven7
 *
 * @param <T> the data
 */
public interface MultiItemTypeSupport<T> {

	/**
	 *  get the layout id
	 * @param position the position of adapter
	 * @param t  the data
     * @return  the layout id for multi item.
     */
	@LayoutRes int getLayoutId(int position, T t);

	/**
	 * get thw view type count.
	 * @return thw view type count.
     */
	int getViewTypeCount();

	/**
	 * get item view type. often from 1.
	 * @param postion the position of  adapter
	 * @param t the data
     * @return the item view type
     */
	int getItemViewType(int postion, T t);
}
