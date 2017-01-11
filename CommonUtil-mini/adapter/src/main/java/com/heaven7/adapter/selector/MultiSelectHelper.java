package com.heaven7.adapter.selector;

import java.util.ArrayList;
import java.util.List;

/**
 * multi mode of select helper
 * Created by heaven7 on 2017/1/9.
 * @since 1.8.5
 */
public class MultiSelectHelper extends AbstractSelectHelper {

    private final List<Integer> mSelectPositions;
    /**
     * the max count of select state.
     */
    private final int mMaxCount;

    public MultiSelectHelper(SelectorNotifier notifier) {
        this(notifier, Integer.MAX_VALUE);
    }

    public MultiSelectHelper(SelectorNotifier notifier, int maxSelectedCount) {
        super(notifier);
        this.mSelectPositions = new ArrayList<>();
        this.mMaxCount = maxSelectedCount;
    }

    @Override
    public void initSelectPosition(List<Integer> positions, boolean notify) {
        mSelectPositions.clear();
        if (positions != null && positions.size() > 0) {
            if (positions.size() > mMaxCount) {
                positions = positions.subList(0, mMaxCount);
            }
            mSelectPositions.addAll(positions);
            if (notify) {
                notifySelectorStateChanged(null, toIntArray(positions));
            }
        }
    }

    @Override
    public boolean select(int position) {
        if (!mSelectPositions.contains(position)) {
            if (mSelectPositions.size() < mMaxCount) {
                mSelectPositions.add(position);
                notifySelectorStateChanged(null, new int[]{position});
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean unselect(int position) {
        if (mSelectPositions.contains(position)) {
            mSelectPositions.remove(position);
            notifySelectorStateChanged(new int[]{position}, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean toggleSelect(int position) {
        boolean result = false;
        if (mSelectPositions.contains(position)) {
            mSelectPositions.remove(position);
            notifySelectorStateChanged(new int[]{position}, null);
            result = true;
        } else {
            if (mSelectPositions.size() < mMaxCount) {
                mSelectPositions.add(position);
                notifySelectorStateChanged(null, new int[]{position});
                result = true;
            }
        }
        return result;
    }

    @Override
    public void clearSelectedPosition() {
        mSelectPositions.clear();
    }

    @Override
    public void clearSelectedState() {
        final int[] positions = toIntArray(mSelectPositions);
        clearSelectedPosition();
        notifySelectorStateChanged(positions, null);
    }

    @Override
    public int[] getSelectPosition() {
        return toIntArray(mSelectPositions);
    }


}
