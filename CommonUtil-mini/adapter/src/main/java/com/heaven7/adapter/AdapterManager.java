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

import android.database.Observable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

import com.heaven7.core.util.ViewHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * the AdapterManager help you manage the adapter.like data, header,footer,IPostRunnableCallback.
 * Created by heaven7 on 2015/11/29.
 */
public class AdapterManager<T extends ISelectable> implements SelectHelper.Callback<T> {

    private final IAdapterManagerCallback2 mCallback2;
    private final SelectHelper<T> mSelectHelper;
    private List<T> mDatas;
    private ArrayList<IPostRunnableCallback<T>> mPostCallbacks;
    private AdapterDataRemovedObservable<T> mRemovedObservable;

    /**
     * @param selectMode see {@link ISelectable#SELECT_MODE_MULTI} or {@link ISelectable#SELECT_MODE_MULTI}
     */
    /*public*/ AdapterManager(List<T> data, int selectMode, IAdapterManagerCallback2 callback2) {
        this.mDatas = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        mSelectHelper = new SelectHelper<T>(selectMode, this);
        mSelectHelper.initSelectPositions(data);
        this.mCallback2 = callback2;
    }

    protected int getHeaderSize() {
        return getHeaderFooterManager() != null ? getHeaderFooterManager().getHeaderSize() : 0;
    }

    /**
     * get the adapter position of RecyclerView. this is only used for {@link RecyclerView}.
     *
     * @param initPosition the init position . which is initialized in {@link QuickRecycleViewAdapter#onBindViewHolder(
     *RecyclerView.ViewHolder, int)} .
     * @return the adapter position of RecyclerView which is the key of ViewHolder map.
     * see {@link RecyclerView.ViewHolder#getAdapterPosition()}
     */
    public int getAdapterPosition(int initPosition) {
        return getSelectHelper().getAdapterPosition(initPosition);
    }

    //======================== data remove listener ===============
    public void registerAdapterDataRemoveObserver(IAdapterDataRemovedObserver<T> observer) {
        if (mRemovedObservable == null) {
            mRemovedObservable = new AdapterDataRemovedObservable<>();
        }
        mRemovedObservable.registerObserver(observer);
    }

    public void unregisterAdapterDataRemoveObserver(IAdapterDataRemovedObserver<T> observer) {
        if (mRemovedObservable != null) {
            mRemovedObservable.unregisterObserver(observer);
        }
    }
    //=====================  post callback  ================

    /***
     * the callbacks of IPostRunnableCallback.
     *
     * @since 1.7.5(pre)
     */
    ArrayList<IPostRunnableCallback<T>> getPostRunnableCallbacks() {
        return mPostCallbacks;
    }

    /**
     * add a IPostRunnableCallback to run the last of bind adapter data.
     *
     * @param callback the post callback
     * @since 1.7.5
     */
    public void addPostRunnableCallback(IPostRunnableCallback<T> callback) {
        if (mPostCallbacks == null) {
            mPostCallbacks = new ArrayList<>(4);
        }
        if (!mPostCallbacks.contains(callback)) {
            this.mPostCallbacks.add(callback);
        }
    }

    /**
     * remove a IPostRunnableCallback to run the last of bind adapter data.
     *
     * @param callback the post callback
     * @since 1.7.5
     */
    public void removePostRunnableCallback(IPostRunnableCallback<T> callback) {
        if (mPostCallbacks != null) {
            mPostCallbacks.remove(callback);
        }
    }

    /**
     * clear the array of  IPostRunnableCallback
     *
     * @since 1.7.5
     */
    public void clearPostRunnableCallbacks() {
        if (mPostCallbacks != null) {
            mPostCallbacks.clear();
        }
    }
    //================================================

    /**
     * perform item change
     *
     * @param index   the index
     * @param changer the item changer
     * @since 1.5.8
     */
    public void performItemChange(int index, ItemChanger<T> changer) {
        if (changer.onItemChange(getItemAt(index))) {
            if (isRecyclable()) {
                notifyItemChanged(index);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * perform item change
     *
     * @param data    the data
     * @param changer the item changer
     * @since 1.5.8
     */
    public void performItemChange(T data, ItemChanger<T> changer) {
        final int index = mDatas.indexOf(data);
        if (index != -1) {
            if (changer.onItemChange(data)) {
                if (isRecyclable()) {
                    notifyItemChanged(index);
                } else {
                    notifyDataSetChanged();
                }
            }
        }
    }
    //================== manage item ===============================

    public void addItem(int index, T item) {
        mDatas.add(index, item);
        if (isRecyclable()) {
            notifyItemInserted(index);
        } else {
            notifyDataSetChanged();
        }
    }

    public void addItem(T item) {
        mDatas.add(item);
        if (isRecyclable()) {
            notifyItemInserted(mDatas.size() - 1);
        } else {
            notifyDataSetChanged();
        }
    }

    public void addItems(T... items) {
        if (items == null || items.length == 0)
            return;
        addItems(Arrays.asList(items));
    }

    public void addItems(int startIndex, Collection<T> items) {
        final int preSize = mDatas.size();
        if (startIndex < 0 || startIndex > preSize) {
            throw new IllegalArgumentException();
        }
        mDatas.addAll(startIndex, items);
        if (isRecyclable()) {
            notifyItemRangeInserted(startIndex, items.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void addItems(Collection<T> items) {
        final int preSize = mDatas.size();
        mDatas.addAll(items);
        if (isRecyclable()) {
            notifyItemRangeInserted(preSize, items.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @RemoveObservableMethod
    public void setItem(T oldItem, T newItem) {
        setItem(mDatas.indexOf(oldItem), newItem);
    }

    @RemoveObservableMethod
    public void setItem(int index, T newItem) {
        if (index < 0 || index > getItemSize() - 1) {
            return;
        }
        if (isRecyclable()) {
            final T t = mDatas.remove(index);
            notifyItemRemovedInternal(t);
            notifyItemRemoved(index);
            //for observable , i changed this implement.
            addItem(index, newItem);
        } else {
            mDatas.set(index, newItem);
            notifyDataSetChanged();
        }
       /* final T t = mDatas.set(index, newItem);
        notifyItemRemovedInternal(t);
        if (isRecyclable()) {
            notifyItemChanged(index + getHeaderSize());
        } else {
            notifyDataSetChanged();
        }*/
    }

    @RemoveObservableMethod
    public void removeItem(T item) {
        removeItem(mDatas.indexOf(item));
    }

    /**
     * remove the index of the target item. you should careful about the index (is the right index of the adapter ?
     * you can get the actual index by calling {@link RecyclerView.ViewHolder#getAdapterPosition()}
     * and you can get view holder by calling {@link android.support.v7.widget.RecyclerView#getChildViewHolder(View)}).
     * but if you can't confirm about the position , please use {@link #removeItemForRecyclerView(View)} intead.
     *
     * @param actualIndex the real index of adapter. you can get the actual index by calling {@link RecyclerView.ViewHolder#getAdapterPosition()}
     */
    @RemoveObservableMethod
    public void removeItem(int actualIndex) {
        if (actualIndex == -1 || actualIndex > getItemSize() - 1) {
            return;
        }
        final T removedItem = mDatas.remove(actualIndex);
        notifyItemRemovedInternal(removedItem);
        if (isRecyclable()) {
            notifyItemRemoved(actualIndex);
        } else {
            notifyDataSetChanged();
        }
    }

    /**
     * remove the item which is indicated by the target item view.
     *
     * @param itemView the item view which is child of RecyclerView.
     * @since 1.8.5
     */
    @RemoveObservableMethod
    public void removeItemForRecyclerView(View itemView) {
        final ViewParent parent = itemView.getParent();
        if (parent != null && parent instanceof RecyclerView) {
            final int position = ((RecyclerView) parent).getChildAdapterPosition(itemView);
            removeItem(position);
        } else {
            throw new IllegalArgumentException("the item view is incorrect. you must check !");
        }
    }

    @RemoveObservableMethod
    public void removeItems(List<T> ts) {
        if (ts == null || ts.size() == 0)
            return;
        List<T> mDatas = this.mDatas;
        for (int i = 0, size = ts.size(); i < size; i++) {
            mDatas.remove(ts.get(i));
        }
        notifyItemRangeRemovedInternal(ts);
        notifyDataSetChanged();
    }

    @RemoveObservableMethod
    public void removeItemsByPosition(List<Integer> positions) {
        if (positions == null || positions.size() == 0)
            return;
        final AdapterDataRemovedObservable<T> mRemovedObservable = this.mRemovedObservable;
        final boolean hasObserver = mRemovedObservable != null && mRemovedObservable.hasObservers();
        List<T> mDatas = this.mDatas;
        int pos;
        T t;
        for (int i = 0, size = positions.size(); i < size; i++) {
            pos = positions.get(i);
            t = mDatas.remove(pos);
            if (hasObserver) {
                mRemovedObservable.addItem(t);
            }
        }
        if (hasObserver) {
            mRemovedObservable.notifyItemRangeRemoved();
        }
        notifyDataSetChanged();
    }

    @RemoveObservableMethod
    public void replaceAllItems(List<T> items) {
        mSelectHelper.clearSelectedState();
        final boolean hasObserver = observeAllItems();
        mDatas.clear();
        mDatas.addAll(items);
        mSelectHelper.initSelectPositions(items);

        if (hasObserver) {
            mRemovedObservable.notifyItemRangeRemoved();
        }
        notifyDataSetChanged();
    }

    @RemoveObservableMethod
    public void clearItems() {
        final boolean hasObserver = observeAllItems();
        mDatas.clear();
        if (hasObserver) {
            mRemovedObservable.notifyItemRangeRemoved();
        }
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return mDatas;
    }

    public boolean containsItem(T item) {
        return mDatas.contains(item);
    }

    @Override
    public final void notifyDataSetChanged() {
        mCallback2.beforeNotifyDataChanged();
        mCallback2.notifyDataSetChanged();
        mCallback2.afterNotifyDataChanged();
    }

    // =========== begin recycleview notify ==============//

    private void checkIfSupport() {
        if (!isRecyclable()) {
            throw new UnsupportedOperationException("only recycle view support");
        }
    }

    public final void notifyItemInserted(int position) {
        checkIfSupport();
        position += getHeaderSize();
        mCallback2.notifyItemInserted(position);
    }

    @Override
    public final void notifyItemChanged(int position) {
        checkIfSupport();
        position += getHeaderSize();
        mCallback2.notifyItemChanged(position);
    }

    /**
     * notify item removed, had handle the header size.
     *
     * @param position the  position
     */
    public final void notifyItemRemoved(int position) {
        checkIfSupport();
        position += getHeaderSize();
        mCallback2.notifyItemRemoved(position);
    }

    /**
     * notify item moved, had handle the header size.
     *
     * @param fromPosition the from position
     * @param toPosition   the end position.
     */
    public final void notifyItemMoved(int fromPosition, int toPosition) {
        checkIfSupport();
        fromPosition += getHeaderSize();
        toPosition += getHeaderSize();
        mCallback2.notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * notify item range changed, had handle the header size.
     *
     * @param positionStart the from position
     * @param itemCount     the item count
     */
    public final void notifyItemRangeChanged(int positionStart, int itemCount) {
        checkIfSupport();
        positionStart += getHeaderSize();
        mCallback2.notifyItemRangeChanged(positionStart, itemCount);
    }

    /**
     * notify item range insert , had handle the header size.
     *
     * @param positionStart the from position
     * @param itemCount     the item count
     */
    public final void notifyItemRangeInserted(int positionStart, int itemCount) {
        checkIfSupport();
        positionStart += getHeaderSize();
        mCallback2.notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * notify item range moved, had handle the header size.
     *
     * @param positionStart the from position
     * @param itemCount     the item count
     */
    public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
        checkIfSupport();
        positionStart += getHeaderSize();
        mCallback2.notifyItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    public boolean isRecyclable() {
        return mCallback2.isRecyclable();
    }

    @Override
    public T getSelectedItemAtPosition(int position) {
        return getItemAt(position);
    }

    //================== ========================//

    public IHeaderFooterManager getHeaderFooterManager() {
        throw new UnsupportedOperationException();
    }

    public int getItemSize() {
        return mDatas.size();
    }

    public T getItemAt(int index) {
        return mDatas.get(index);
    }

    public SelectHelper<T> getSelectHelper() {
        return mSelectHelper;
    }

    /**
     * observe the current all items by the AdapterDataRemovedObservable.
     *
     * @return true if has observer.
     * @since 1.5.7
     */
    private boolean observeAllItems() {
        AdapterDataRemovedObservable<T> mRemovedObservable = AdapterManager.this.mRemovedObservable;
        if (mRemovedObservable != null && mRemovedObservable.hasObservers()) {
            mRemovedObservable.addItems(mDatas);
            return true;
        }
        return false;
    }

    private void notifyItemRemovedInternal(T removedItem) {
        if (mRemovedObservable != null && mRemovedObservable.hasObservers()) {
            mRemovedObservable.notifyItemRemoved(removedItem);
        }
    }

    private void notifyItemRangeRemovedInternal(List<T> removedItems) {
        if (mRemovedObservable != null && mRemovedObservable.hasObservers()) {
            mRemovedObservable.notifyItemRangeRemoved(removedItems);
        }
    }

    /**
     * the adapter data observer.
     * <p>notify item by ourselves. or else can't fix some problem of count down timer</p>
     *
     * @since 1.5.7
     */
    public interface IAdapterDataRemovedObserver<T> {

        /**
         * called when one or more item is removed.
         *
         * @param items the items
         */
        void onItemRangeRemoved(List<T> items);
    }

    /**
     * the internal data removed observable
     *
     * @param <T> the data type
     * @since 1.5.7
     */
    static class AdapterDataRemovedObservable<T> extends Observable<IAdapterDataRemovedObserver<T>> {

        private final ArrayList<T> removedItems = new ArrayList<>();

        public void addItem(T t) {
            removedItems.add(t);
        }

        public void addItems(List<T> items) {
            removedItems.addAll(items);
        }

        public void clear() {
            removedItems.clear();
        }

        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        /**
         * notify item removed. after this the pool of removed items will clear.
         *
         * @param items the items data
         */
        public void notifyItemRangeRemoved(List<T> items) {
            addItems(items);
            notifyItemRangeRemoved();
        }

        /**
         * notify item removed. after this the pool of removed items will clear.
         *
         * @param t the item data
         */
        public void notifyItemRemoved(T t) {
            addItem(t);
            notifyItemRangeRemoved();
        }

        /**
         * notify item removed. after this the pool of removed items will clear.
         */
        public void notifyItemRangeRemoved() {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeRemoved(removedItems);
            }
            removedItems.clear();
        }
    }


    /**
     * ths supporter of header and footer, call any method will automatic call
     * {@link QuickRecycleViewAdapter#notifyDataSetChanged()}.
     * in recyclerView .this is only can be used by LinearLayoutManager
     */
    public interface IHeaderFooterManager {

        void addHeaderView(View v);

        void removeHeaderView(View v);

        void addFooterView(View v);

        void removeFooterView(View v);

        int getHeaderSize();

        int getFooterSize();

        boolean isFooter(int position);

        boolean isHeader(int position);
    }

    public interface IAdapterManagerCallback<T extends ISelectable> {

        AdapterManager<T> getAdapterManager();
    }

    /**
     * a callback will run after quickAdapter.onBindData(...) and in Runnable .
     * this is useful when you need post a runnable at last of bind adapter data in every position.
     *
     * @param <T> the entity
     */
    public interface IPostRunnableCallback<T extends ISelectable> {

        /**
         * called in every position's bind data.
         *
         * @param position     the position
         * @param item         the data
         * @param itemLayoutId the layout id
         * @param helper       the view helper
         */
        void onPostCallback(int position, T item, int itemLayoutId, ViewHelper helper);
    }

    interface IAdapterManagerCallback2 {
        void notifyItemInserted(int position);

        void notifyItemChanged(int position);

        void notifyItemRemoved(int position);

        void notifyItemMoved(int fromPosition, int toPosition);

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void notifyItemRangeInserted(int positionStart, int itemCount);

        void notifyItemRangeRemoved(int positionStart, int itemCount);

        // =========== end recycleview ==============//

        void notifyDataSetChanged();

        boolean isRecyclable();

        /**
         * this called before {@link #notifyDataSetChanged()}
         */
        void beforeNotifyDataChanged();

        /**
         * this called after {@link #notifyDataSetChanged()}
         */
        void afterNotifyDataChanged();
    }


    static abstract class SimpleAdapterManagerCallback2 implements IAdapterManagerCallback2 {

        public void notifyItemInserted(int position) {
        }

        public void notifyItemChanged(int position) {
        }

        public void notifyItemRemoved(int position) {
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        }

    }

    /**
     * indicate the method is observable for {@link AdapterDataRemovedObservable}
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface RemoveObservableMethod {
    }

    /**
     * the item changer
     *
     * @param <T>
     */
    public interface ItemChanger<T> {
        /**
         * called when the item need change.
         *
         * @param t the item data which may be changed
         * @return true if change data success , if success notify will called.
         */
        boolean onItemChange(T t);
    }

}
