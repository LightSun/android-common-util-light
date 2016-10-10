package com.heaven7.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.core.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * i extend it.
 * thanks to xiaofan
 *
 * @author heaven7
 */
public class BaseFragmentPagerAdapter extends PagerAdapter {

    private static final String TAG = "FragmentStatePagerAdapter";
    private static final boolean DEBUG = false;

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private SparseArray<Fragment.SavedState> mSavedState = new SparseArray<Fragment.SavedState>();
    private final HashMap<FragmentData, ItemData> mCache = new HashMap<FragmentData, ItemData>();
    private final List<FragmentData> mFragmentDatas;

    private List<FragmentData> mTempList;
    private IPageChangeListener mListener;
    private boolean mBegined;

    public BaseFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull List<FragmentData> fragmentDatas) {
        this.mFragmentManager = fm;
        this.mFragmentDatas = fragmentDatas;
    }

    public IPageChangeListener getOnPagerChangeListener() {
        return mListener;
    }

    public void setPageChangeListener(IPageChangeListener mListener) {
        this.mListener = mListener;
    }

    /**
     * Sorts the given list in ascending natural order. The algorithm is
     * stable which means equal elements don't get reordered.
     *
     * @since 1.6.2
     */
    public void sort() {
        sort(null);
    }

    /**
     * sort the fragment with the target comparator,
     *
     * @param comparator the Comparator, null means Sorts the given list in ascending natural order.
     * @since 1.6.2
     */
    public void sort(Comparator<FragmentData> comparator) {
        checkTransaction();
        saveOldOrder();
        if (comparator == null) {
            Collections.sort(mFragmentDatas);
        } else {
            Collections.sort(this.mFragmentDatas, comparator);
        }
        checkPositionChange();
    }

    private void checkPositionChange() {
        //handle position with save state
        final SparseArray<Fragment.SavedState> temp = new SparseArray<>();
        final IPageChangeListener l = getOnPagerChangeListener();

        boolean changed = false;
        FragmentData data;
        for (int i = 0, size = mFragmentDatas.size(); i < size; i++) {
            data = mFragmentDatas.get(i);
            int oldPos = mTempList.indexOf(data);
            if (oldPos != i) {
                changed = true;
                final ItemData itemData = mCache.get(data);
                if (itemData != null) {
                    itemData.position = i;
                }
                final Fragment.SavedState state = mSavedState.get(oldPos);
                if (state != null) {
                    temp.put(i, state);
                }
                if (l != null) {
                    l.onPositionChanged(this, data, oldPos, i);
                }
            }
        }
        mSavedState = temp;
        if (changed) {
            notifyDataSetChanged();
        }
    }

    private void saveOldOrder() {
        //save old pos
        if (mTempList == null) {
            mTempList = new ArrayList<>();
        } else {
            mTempList.clear();
        }
        mTempList.addAll(mFragmentDatas);
    }

    /**
     * add a FragmentData
     *
     * @param data the fragment data
     * @since 1.6.2
     */
    public void addFragmentData(FragmentData data) {
        addFragmentDataImpl(getCount(), data, true);
    }

    /**
     * use {@link #addFragmentData(int, FragmentData)} instead.
     * add a FragmentData by target position.
     *
     * @param data     the fragment data
     * @param position the position to insert/add
     * @since 1.6.2
     */
    @Deprecated
    public void addFragmentData(FragmentData data, int position) {
        addFragmentDataImpl(position, data , true);
    }

    /**
     * add a FragmentData by target position.
     *
     * @param data     the fragment data
     * @param position the position to insert/add
     * @since 1.6.2
     */
    public void addFragmentData(int position, FragmentData data){
        addFragmentDataImpl(position, data , true);
    }
    /**
     * add  FragmentData
     *
     * @param datas the fragment datas
     * @param position the position to add
     * @since 1.6.2
     */
    public void addFragmentData(int position ,List<FragmentData> datas) {
        addFragmentDataImpl(position, datas, true);
    }
    /**
     * add  FragmentData
     *
     * @param datas the fragment datas
     * @since 1.6.2
     */
    public void addFragmentData(List<FragmentData> datas) {
        addFragmentDataImpl(getCount(), datas, true);
    }


    /**
     * remove a FragmentData add notify
     *
     * @param data the fragment data
     * @since 1.6.2
     */
    public void removeFragmentData(FragmentData data) {
        removeFragmentDataImpl(data , true);
    }

    /**
     * remove list of  FragmentData
     *
     * @param datas the fragment datas
     * @since 1.6.2
     */
    public void removeFragmentData(List<FragmentData> datas) {
       removeFragmentDataImpl(datas, true);
    }

    /**
     * get the current fragment data.
     *
     * @return the current ftagment data
     * @since 1.6.2
     */
    public List<FragmentData> getFragmentDatas() {
        return mFragmentDatas;
    }

    private void addFragmentDataImpl(int position, List<FragmentData> datas, boolean notify) {
        if (datas == null || position < 0 || position > mFragmentDatas.size()) {
            throw new IllegalArgumentException();
        }
        final List<FragmentData> mFragmentDatas = this.mFragmentDatas;
        if( notify ) {
            final IPageChangeListener l = this.getOnPagerChangeListener();
            int index = position;
            for (FragmentData data : datas) {
                mFragmentDatas.add(index, data);
                if ( l != null) {
                    l.onAdd(this, data, index);
                }
                index ++ ;
            }
            notifyDataSetChanged();
        }else{
            mFragmentDatas.addAll(position, datas);
        }
    }

    private void addFragmentDataImpl(int position, FragmentData data, boolean notify) {
        if (position < 0 || position > mFragmentDatas.size()) {
            throw new IllegalArgumentException();
        }
        mFragmentDatas.add(position, data);
        if(notify) {
            if (mListener != null) {
                mListener.onAdd(this, data, position);
                for (int i = position + 1, size = mFragmentDatas.size(); i < size; i++) {
                    mListener.onPositionChanged(this, mFragmentDatas.get(i), i - 1, i);
                }
            }
            notifyDataSetChanged();
        }
    }

    private void removeFragmentDataImpl(FragmentData data , boolean notify) {
        final int index = mFragmentDatas.indexOf(data);
        if (index != -1) {
            mFragmentDatas.remove(index);
            if(notify) {
                if (mListener != null) {
                    mListener.onRemove(this, data, index);
                }
                notifyDataSetChanged();
            }
        }
    }

    private void removeFragmentDataImpl(List<FragmentData> datas, boolean notify) {
        if (datas == null) {
            throw new NullPointerException();
        }
        final List<FragmentData> mFragmentDatas = this.mFragmentDatas;
        if(notify) {
            final IPageChangeListener l = this.getOnPagerChangeListener();
            for (FragmentData data : datas) {
                int index = mFragmentDatas.indexOf(data);
                if (index == -1)
                    continue;
                mFragmentDatas.remove(index);
                if (l != null) {
                    l.onRemove(this, data, index);
                }
            }
            notifyDataSetChanged();
        }else{
            for (FragmentData data : datas) {
                mFragmentDatas.remove(data);
            }
        }
    }
    //=============================================================== //

    @Override
    public final int getCount() {
        return mFragmentDatas.size();
    }

    @Override
    public final int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void startUpdate(ViewGroup container) {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // If we already have this item instantiated, there is nothing
        // to do. This can happen when we are restoring the entire pager
        // from its saved state, where the fragment manager has already
        // taken care of restoring the fragments we previously had instantiated.
        FragmentData data = mFragmentDatas.get(position);
        ItemData item = mCache.get(data);
        if (item != null) {
            item.position = position;
            if (item.fragment.getView().getParent() == null)
                container.addView(item.fragment.getView());
            return item;
        }

        Fragment fragment = Fragment.instantiate(container.getContext(),
                data.fragmentClass.getName(), data.bundle);
        item = new ItemData(data, fragment, position);
        mCache.put(data, item);
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG)
            Logger.v(TAG, "Adding item #" + position + ": f=" + fragment);
        //	if (mSavedState.size() > position) {
        Fragment.SavedState fss = mSavedState.get(position);
        if (fss != null) {
            fragment.setInitialSavedState(fss);
        }
        fragment.setMenuVisibility(false);
        fragment.setUserVisibleHint(false);
        mCurTransaction.add(container.getId(), fragment);
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ItemData itemData = (ItemData) object;
        if (dispatchNeedDestroy(position, itemData.data, itemData.fragment)) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            if (DEBUG)
                Logger.v(TAG, "Removing item #" + position + ": f=" + object +
                        " v=" + ((Fragment) object).getView());
            mSavedState.put(position, mFragmentManager.saveFragmentInstanceState(itemData.fragment));
            mCurTransaction.remove(itemData.fragment);
            mCache.remove(itemData.data);
        } else {
            container.removeView(itemData.fragment.getView());
        }
    }

    protected boolean dispatchNeedDestroy(int position, FragmentData data, Fragment fragment) {
        return !mFragmentDatas.contains(data) || needDestroy(position, data, fragment);
    }

    protected boolean needDestroy(int position, FragmentData data, Fragment fragment) {
        return false;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        ItemData itemData = (ItemData) object;
        if (itemData == null) {
            return;
        }
        Fragment fragment = itemData.fragment;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((ItemData) object).fragment.getView() == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentDatas.get(position).title;
    }

    /**
     * return the fragment of target position. if target Fragment not create , return null.
     * use {@link #getFragment(int)} instead.
     *
     * @param position the position
     * @return fragment
     */
    @Deprecated
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    /**
     * return the fragment of target position. if target Fragment not create , return null.
     *
     * @param position the position
     * @return fragment
     */
    public Fragment getFragment(int position) {
        FragmentData data = mFragmentDatas.get(position);
        ItemData item = mCache.get(data);
        return item != null ? item.fragment : null;
    }

    /**
     * start a pager adapter data transaction . you must call {@link Transaction#commit()} after 'CRUD' operation.
     * @return PagerAdapterTransaction
     */
    public PagerAdapterTransaction beginTransaction(){
        checkTransaction();
        mBegined = true;
        saveOldOrder();
        return new Transaction();
    }

    private void checkTransaction() {
        if(mBegined){
            throw new IllegalStateException("you must call commit() of last transaction");
        }
    }


    /**
     * contains: title, fragmentClass and fragment args(arguments)
     */
    public static class FragmentData implements Comparable<FragmentData> {
        public String title;
        public Class<?> fragmentClass;
        public Bundle bundle;
        /**
         * this is only used in {@link BaseFragmentPagerAdapter#sort(Comparator)}
         */
        public int priority;

        public FragmentData(String title, Class<?> fragmentClass, Bundle bundle) {
            super();
            this.title = title;
            this.fragmentClass = fragmentClass;
            this.bundle = bundle;
        }

        @Override
        public int compareTo(@NonNull FragmentData another) {
            return this.priority < another.priority ? -1 :
                    (this.priority == another.priority ? 0 : 1);
            //return Integer.compare(this.priority, another.priority);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FragmentData that = (FragmentData) o;
            return title.equals(that.title);
        }

        @Override
        public int hashCode() {
            return title.hashCode();
        }
    }

    /**
     * contains, data, fragment, position
     */
    private static class ItemData {
        FragmentData data;
        Fragment fragment;
        int position;

        public ItemData(FragmentData data, Fragment fragment, int position) {
            super();
            this.fragment = fragment;
            this.data = data;
            this.position = position;
        }
    }

    /**
     * the page chang listener.
     */
    public interface IPageChangeListener {

        /**
         * called after remove a FragmentData.
         *
         * @param adapter the adapter
         * @param data    the fragment data
         * @param pos    the position
         */
        void onRemove(BaseFragmentPagerAdapter adapter, FragmentData data, int pos);

        /**
         * called after add a FragmentData.
         *
         * @param adapter the adapter
         * @param data    the fragment data
         * @param pos     old position
         */
        void onAdd(BaseFragmentPagerAdapter adapter, FragmentData data, int pos);

        /**
         * called on position changed
         *
         * @param adapter the adapter
         * @param data    the fragment data
         * @param oldPos  old position
         * @param newPos  new position
         */
        void onPositionChanged(BaseFragmentPagerAdapter adapter, FragmentData data, int oldPos, int newPos);
    }

    private class Transaction extends PagerAdapterTransaction{

        @Override
        public PagerAdapterTransaction addFragmentData(FragmentData data) {
            addFragmentDataImpl(getCount(), data, false);
            return this;
        }

        @Override
        public PagerAdapterTransaction addFragmentData(int position, FragmentData data) {
            addFragmentDataImpl(position, data, false);
            return this;
        }

        @Override
        public PagerAdapterTransaction addFragmentData(int position, List<FragmentData> datas) {
            addFragmentDataImpl(position, datas, false);
            return this;
        }

        @Override
        public PagerAdapterTransaction addFragmentData(List<FragmentData> datas) {
            addFragmentDataImpl(getCount(), datas, false);
            return this;
        }

        @Override
        public PagerAdapterTransaction removeFragmentData(FragmentData data) {
            removeFragmentDataImpl(data, false);
            return this;
        }

        @Override
        public PagerAdapterTransaction removeFragmentData(List<FragmentData> datas) {
            removeFragmentDataImpl(datas, false);
            return this;
        }

        @Override
        public PagerAdapterTransaction performSort(Comparator<FragmentData> comparator, ISortPerformer performer) {
            performer.performSort(BaseFragmentPagerAdapter.this, getFragmentDatas(), comparator);
            return this;
        }

        @Override
        public PagerAdapterTransaction performUpdate(IUpdatePerformer performer) {
            performer.performUpdate(BaseFragmentPagerAdapter.this, getFragmentDatas());
            return this;
        }

        @Override
        public void commit() {
            final List<FragmentData> oldList = mTempList;
            final List<FragmentData> newList = mFragmentDatas;
            final IPageChangeListener l = mListener;
            //add /remove /pos_change
            if( l != null) {
                final BaseFragmentPagerAdapter adapter = BaseFragmentPagerAdapter.this;
                for (int size = newList.size(), i = size - 1; i < size; i--) {
                    final FragmentData data = newList.get(i);
                    final int oldIndex = oldList.indexOf(data);
                    if (oldIndex == -1) {
                        l.onAdd(adapter, data, i);
                    }else if(i != oldIndex){
                        l.onPositionChanged(adapter, data, oldIndex, i);
                    }
                }
                for (int size = oldList.size(), i = size - 1; i < size; i--) {
                    final FragmentData data = oldList.get(i);
                    final int newIndex = newList.indexOf(data);
                    if (newIndex == -1) {
                        l.onRemove(adapter, data, i);
                    }
                }
            }
            notifyDataSetChanged();
            mBegined = false;
        }
    }
}
