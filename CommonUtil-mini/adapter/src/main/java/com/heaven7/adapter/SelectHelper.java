package com.heaven7.adapter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.heaven7.adapter.selector.MultiSelectHelper;
import com.heaven7.adapter.selector.SingleSelectHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.ViewHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * a class help we handle the select state. support single-select or multi-select.
 * Created by heaven7 on 2015/9/3.
 */
public class SelectHelper<T extends ISelectable> {

    private static final String TAG = "SelectHelper";
    private static final boolean DEBUG = false;
    private final
    @ModeType
    int mSelectMode;

    private final Callback<T> mCallback;
    private final ISelectHelper mImpl;
    private SparseArray<WeakReference<RecyclerView.ViewHolder>> mHolderMap;

    private List<T> mSelectDatas;

    @IntDef({
            ISelectable.SELECT_MODE_MULTI,
            ISelectable.SELECT_MODE_SINGLE,
    })
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @interface ModeType {
    }

    private class NotifierImpl implements ISelectHelper.SelectorNotifier {
        /**
         * notify impl. and return a flag to indicate whether notify all items or not.
         *
         * @param positions the positions
         * @param selected  true is the selected state ,false is unselected state,
         * @return true if should notify all.
         */
        boolean notifyImpl(int[] positions, boolean selected) {
            if (positions == null || positions.length == 0) {
                return false;
            }
            //adjust the position to the right position.
            adjustPosition(positions);
            final Callback<T> mCallback = SelectHelper.this.mCallback;
            if (mCallback.isRecyclable()) {
                for (int pos : positions) {
                    mCallback.getSelectedItemAtPosition(pos).setSelected(selected);
                    mCallback.notifyItemChanged(pos);
                }
                return false;
            } else {
                for (int pos : positions) {
                    mCallback.getSelectedItemAtPosition(pos).setSelected(selected);
                }
                return true;
            }
        }

        @Override
        public void notifySelectorStateChanged(int[] unselectPostions, int[] selectPostions) {
            if (notifyImpl(unselectPostions, false) || notifyImpl(selectPostions, true)) {
                mCallback.notifyDataSetChanged();
            }
        }
    }

    /**
     * @param callback   the callabck
     * @param selectMode the select mode
     * @since 1.7.5
     */
    public SelectHelper(@ModeType int selectMode, Callback<T> callback) {
        NotifierImpl mNotifier = new NotifierImpl();
        mImpl = selectMode == ISelectable.SELECT_MODE_MULTI ? new MultiSelectHelper(mNotifier)
                : new SingleSelectHelper(mNotifier);
        if (selectMode != ISelectable.SELECT_MODE_SINGLE &&
                selectMode != ISelectable.SELECT_MODE_MULTI) {
            throw new IllegalArgumentException("invalid select mode = " + selectMode);
        }
        this.mSelectMode = selectMode;
        this.mCallback = callback;
    }

    /**
     * use  {@link #select(int)} instead.
     *
     * @param position the position of adapter
     */
    @Deprecated
    public void setSelected(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI)
            return;
        mImpl.select(position);
    }

    /**
     * select the target position of the item.if currentPosition  == position.ignore it.
     *
     * @param position the target position
     */
    public boolean select(int position) {
        return mImpl.select(position);
    }

    /**
     * unselect the target position.
     * {@link ISelectable#SELECT_MODE_MULTI} and {@link ISelectable#SELECT_MODE_SINGLE} both support
     *
     * @param position the position
     * @return true if unselect  success.
     */
    public boolean unselect(int position) {
        return mImpl.unselect(position);
    }

    /**
     * <p>Use {@link #unselect(int)} instead.</p>
     *
     * @param position the position
     **/
    @Deprecated
    public void setUnselected(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI)
            return;
        mImpl.unselect(position);
    }

    /**
     * <p>Use {@link #unselect(int)} instead.</p>
     *
     * @param position the position
     **/
    @Deprecated
    public void addUnselected(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        mImpl.unselect(position);
    }

    /**
     * <p>Use {@link #select(int)} instead.</p>
     *
     * @param selectPosition the select position
     **/
    @Deprecated
    public void addSelected(int selectPosition) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        mImpl.select(selectPosition);
    }

    /**
     * clear the select state but not notify data changed.
     */
    public void clearSelectedPosition() {
        mImpl.clearSelectedPosition();
    }

    /**
     * clear the all select state and notify data changed.
     */
    public void clearSelectedState() {
        mImpl.clearSelectedState();
    }

    /**
     * <p>Use {@link #clearSelectedPosition()} instead.</p>
     * clear the select state but not notify data changed.
     */
    @Deprecated
    public void clearSelectedPositions() {
        mImpl.clearSelectedPosition();
    }

    /**
     * <p>Use {@link #clearSelectedState()} instead.</p>
     * clear the all selected state  and notify data change.
     */
    @Deprecated
    public void clearAllSelected() {
        mImpl.clearSelectedState();
    }

    /**
     * use {@link #toggleSelect(int)} instead.
     * toggle the all selected state and notify data change.
     *
     * @param position the position
     */
    @Deprecated
    public void toogleSelected(int position) {
        toggleSelect(position);
    }

    /**
     * toggle the all selected state and notify data change.
     *
     * @param position the position
     * @return true if toggle this select success.
     */
    public boolean toggleSelect(int position) {
        return mImpl.toggleSelect(position);
    }

    public T getSelectedItem() {
        final int[] poss = getSelectPosition();
        if (poss == null || poss.length == 0) {
            return null;
        }
        return mCallback.getSelectedItemAtPosition(poss[0]);
    }

    /**
     * use {@link #getSelectPosition()} instead.
     *
     * @return the select position.
     */
    @Deprecated
    public int getSelectedPosition() {
        final int[] poss = getSelectPosition();
        if (poss == null || poss.length == 0) {
            return ISelectable.INVALID_POSITION;
        }
        return poss[0];
    }

    /**
     * get the all select position as array.
     *
     * @return the all select position .
     */
    public int[] getSelectPosition() {
        final int[] ints = mImpl.getSelectPosition();
        adjustPosition(ints);
        return ints;
    }

    /**
     * use {@link #getSelectPosition()} instead.
     *
     * @return the all select position.
     */
    @Deprecated
    public List<Integer> getSelectedPositions() {
        final int[] poss = getSelectPosition();
        if (poss == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0, size = poss.length; i < size; i++) {
            list.add(poss[i]);
        }
        return list;
    }

    public List<T> getSelectedItems() {
        final int[] ints = getSelectPosition();
        if (ints == null || ints.length == 0)
            return null;
        if (mSelectDatas == null) {
            mSelectDatas = new ArrayList<>();
        }
        final List<T> mSelectDatas = this.mSelectDatas;
        final Callback<T> mCallback = this.mCallback;
        mSelectDatas.clear();

        for (int i = 0, size = ints.length; i < size; i++) {
            mSelectDatas.add(mCallback.getSelectedItemAtPosition(ints[i]));
        }
        return mSelectDatas;
    }

    public void initSelectPositions(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        //clear record.
        if (mHolderMap != null) {
            mHolderMap.clear();
        }
        //init select positions
        final List<Integer> poss = new ArrayList<>();
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).isSelected()) {
                poss.add(i);
            }
        }
        mImpl.initSelectPosition(poss, false);
    }

    /**
     * clear the view holder records.
     * @since 1.8.6
     */
    /*public*/ void clearViewHolders() {
        if (mHolderMap != null) {
            mHolderMap.clear();
        }
    }

    /**
     * this must be called before {@link QuickRecycleViewAdapter#onBindData(Context, int, ISelectable, int, ViewHelper)}.
     *
     * @param holder the holder.
     * @since 1.8.5
     */
    /*public*/ void onBindViewHolder(RecyclerView.ViewHolder holder) {
        if (mHolderMap == null) {
            mHolderMap = new SparseArray<WeakReference<RecyclerView.ViewHolder>>();
        }
        mHolderMap.put(holder.getAdapterPosition(), new WeakReference<RecyclerView.ViewHolder>(holder));
    }

    /**
     * get the adapter position of RecyclerView. this is only used for {@link RecyclerView}.
     *
     * @param lastBindPosition the last bind position . which is set in {@link QuickRecycleViewAdapter#onBindViewHolder(
     *RecyclerView.ViewHolder, int)} .
     * @return the adapter position of RecyclerView which is the key of ViewHolder map.
     * see {@link RecyclerView.ViewHolder#getAdapterPosition()}
     * @since 1.8.6
     */
    /*public*/ int getAdapterPosition(int lastBindPosition) {
        if (mHolderMap != null) {
            WeakReference<RecyclerView.ViewHolder> ref = mHolderMap.get(lastBindPosition);
            if (ref != null) {
                RecyclerView.ViewHolder holder = ref.get();
                if (holder != null) {
                    return holder.getAdapterPosition();
                }
            }
        }
        return ISelectable.INVALID_POSITION;
    }

    /**
     * adjust the positions. if the adapter of RecyclerView deleted some items util call
     * {@link RecyclerView.Adapter#notifyDataSetChanged()}, that means some items' position should be changed.
     * so this just do it.
     * @param poss the positions to be adjust.
     * @since 1.8.5
     */
    protected void adjustPosition(int[] poss) {
        if (mHolderMap == null || mHolderMap.size() == 0 || poss == null || poss.length == 0) {
            //no need. eg: ListView
            return;
        }
        final SparseArray<WeakReference<RecyclerView.ViewHolder>> mHolderMap = this.mHolderMap;
        WeakReference<RecyclerView.ViewHolder> ref;
        int expectPos;
        for (int size = poss.length, i = size - 1; i >= 0; i--) {
            expectPos = poss[i];
            ref = mHolderMap.get(expectPos);
            if (ref != null) {
                RecyclerView.ViewHolder holder = ref.get();
                if (holder != null) {
                    //adjust to the right position
                    poss[i] = holder.getAdapterPosition();
                    if (DEBUG) {
                        Logger.i(TAG, "adjustPosition", "adjust the position success : expectPos = " + expectPos
                                + " ,right pos = " + holder.getAdapterPosition());
                    }
                } else {
                    //trim
                    mHolderMap.remove(expectPos);
                }
            }
        }
    }

    /**
     * @param <T> the data
     * @since 1.7.5
     */
    public interface Callback<T> {
        /**
         * indicate it use BaseAdapter/BaseExpandableListAdapter or QuickRecycleViewAdapter
         *
         * @return is recyclable
         */
        boolean isRecyclable();

        /**
         * update the datas of adapter
         */
        void notifyDataSetChanged();

        /**
         * only used for  RecycleViewAdapter
         *
         * @param itemPosition the position of item
         */
        void notifyItemChanged(int itemPosition);

        T getSelectedItemAtPosition(int position);
    }

}
