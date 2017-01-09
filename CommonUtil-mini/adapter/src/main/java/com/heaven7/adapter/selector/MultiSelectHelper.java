package com.heaven7.adapter.selector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by heaven7 on 2017/1/9.
 */
public class MultiSelectHelper extends AbstractSelectHelper {

    private final Set<Integer> mSelectPositions;

    public MultiSelectHelper(SelectorNotifier notifier) {
        super(notifier);
        mSelectPositions = new HashSet<>();
    }

    @Override
    public void select(int position) {
        if (mSelectPositions.add(position)) {
            notifySelectorState(null, new int[]{position});
        }
    }

    @Override
    public void unselect(int position) {
        if (mSelectPositions.remove(position)) {
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
        final int size = mSelectPositions.size();
        int[] unselects = new int[size];
        final Object[] arr =  mSelectPositions.toArray();
        for (int i = size - 1; i >= 0; i--) {
            unselects[i] = Integer.valueOf(arr[i].toString());
        }
        notifySelectorState(unselects, null);
    }
}
