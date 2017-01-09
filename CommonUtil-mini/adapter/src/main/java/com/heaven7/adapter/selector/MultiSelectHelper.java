package com.heaven7.adapter.selector;

import java.util.ArrayList;
import java.util.List;

/**
 * multi mode of select helper
 * Created by heaven7 on 2017/1/9.
 */
public class MultiSelectHelper extends AbstractSelectHelper {

    private final List<Integer> mSelectPositions;

    public MultiSelectHelper(SelectorNotifier notifier) {
        super(notifier);
        mSelectPositions = new ArrayList<>();
    }

    @Override
    public void initSelectPosition(List<Integer> positions, boolean notify) {
        mSelectPositions.clear();
        if (positions != null && positions.size() > 0) {
            mSelectPositions.addAll(positions);
            if (notify) {
                notifySelectorState(null, toIntArray(positions));
            }
        }
    }

    @Override
    public void select(int position) {
        if (!mSelectPositions.contains(position)) {
            mSelectPositions.add(position);
            notifySelectorState(null, new int[]{position});
        }
    }

    @Override
    public void unselect(int position) {
        if (mSelectPositions.contains(position)) {
            mSelectPositions.remove(position);
            notifySelectorState(new int[]{position}, null);
        }
    }

    @Override
    public void toggleSelect(int position) {
        if (mSelectPositions.contains(position)) {
            mSelectPositions.remove(position);
            notifySelectorState(new int[]{position}, null);
        } else {
            mSelectPositions.add(position);
            notifySelectorState(null, new int[]{position});
        }
    }

    @Override
    public void clearSelectedPosition() {
        mSelectPositions.clear();
    }

    @Override
    public void clearSelectedState() {
        final int[] positions = getSelectPosition();
        clearSelectedPosition();
        notifySelectorState(positions, null);
    }

    @Override
    public int[] getSelectPosition() {
        return toIntArray(mSelectPositions);
    }


}
