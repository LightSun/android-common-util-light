package com.heaven7.adapter;

import android.support.annotation.IntDef;

import com.heaven7.adapter.selector.MultiSelectHelper;
import com.heaven7.adapter.selector.SingleSelectHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * a class help we handle the select state. support single-select or multi-select.
 * Created by heaven7 on 2015/9/3.
 */
public class SelectHelper<T extends ISelectable> {

    private static final String TAG = "SelectHelper";
    private final
    @ModeType
    int mSelectMode;

    private final Callback<T> mCallback;
    private final ISelectHelper mImpl;

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
         * should notify the all data changed.
         */
        boolean mShouldNotifyAll;

        @Override
        public void begin() {
        }

        @Override
        public void end() {
            if (mShouldNotifyAll) {
                mShouldNotifyAll = false;
                mCallback.notifyDataSetChanged();
            }
        }

        @Override
        public void notifyItemSelected(int[] positions) {
            notifyImpl(positions, true);
        }

        @Override
        public void notifyItemUnselected(int[] positions) {
            notifyImpl(positions, false);
        }

        private void notifyImpl(int[] positions, boolean selected) {
            if (positions == null || positions.length == 0) {
                return;
            }
            final Callback<T> mCallback = SelectHelper.this.mCallback;
            if (mCallback.isRecyclable()) {
                for (int pos : positions) {
                    mCallback.getSelectedItemAtPosition(pos).setSelected(selected);
                    mCallback.notifyItemChanged(pos);
                }
            } else {
                for (int pos : positions) {
                    mCallback.getSelectedItemAtPosition(pos).setSelected(selected);
                }
                if (!mShouldNotifyAll) {
                    mShouldNotifyAll = true;
                }
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
    public void select(int position) {
        mImpl.select(position);
    }

    /**
     * {@link ISelectable#SELECT_MODE_MULTI} and {@link ISelectable#SELECT_MODE_SINGLE} both support
     *
     * @param position the position
     */
    public void unselect(int position) {
        mImpl.unselect(position);
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
     * use {@link #toggleSelected(int)} instead.
     * toggle the all selected state and notify data change.
     *
     * @param position the position
     */
    @Deprecated
    public void toogleSelected(int position) {
        toggleSelected(position);
    }

    /**
     * toggle the all selected state and notify data change.
     *
     * @param position the position
     */
    public void toggleSelected(int position) {
        mImpl.toggleSelect(position);
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
        return mImpl.getSelectPosition();
    }

    /**
     * use {@link #getSelectPosition()} instead.
     *
     * @return the all select position.
     */
    @Deprecated
    public List<Integer> getSelectedPositions() {
        final int[] poss = mImpl.getSelectPosition();
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
        final List<Integer> poss = new ArrayList<>();
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).isSelected()) {
                poss.add(i);
            }
        }
        mImpl.initSelectPosition(poss, false);
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
