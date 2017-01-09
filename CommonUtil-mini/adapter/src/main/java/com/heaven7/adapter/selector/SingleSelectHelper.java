package com.heaven7.adapter.selector;

import java.util.List;

/**
 * single mode of select helper
 * Created by heaven7 on 2017/1/9.
 */
public class SingleSelectHelper extends AbstractSelectHelper {

    private static final String TAG = SingleSelectHelper.class.getSimpleName();

    private int mSelectPosition = NO_POSITION;

    public SingleSelectHelper(SelectorNotifier callback) {
        super(callback);
    }

    @Override
    public void initSelectPosition(List<Integer> positions, boolean notify) {
        mSelectPosition = NO_POSITION;
        if (positions != null && positions.size() > 0) {
            mSelectPosition = positions.get(positions.size() - 1);
            if (notify) {
                notifySelectorState(null, new int[]{mSelectPosition});
            }
        }
    }

    @Override
    public void select(int position) {
        checkPosition(position);
        if (mSelectPosition == position) {
            return;
        }
        final int prePos = mSelectPosition;
        this.mSelectPosition = position;
        int[] unselects = prePos != NO_POSITION ? new int[]{prePos} : null;
        notifySelectorState(unselects, new int[]{position});
    }

    @Override
    public void unselect(int position) {
        checkPosition(position);
        if (mSelectPosition != NO_POSITION && mSelectPosition == position) {
            mSelectPosition = NO_POSITION;
            notifySelectorState(new int[]{position}, null);
        }
    }

    @Override
    public void toggleSelect(int position) {
        checkPosition(position);
        int[] unselects = null;
        int[] selects = null;
        if (mSelectPosition == position) {
            mSelectPosition = NO_POSITION;
            unselects = new int[]{position};
        } else {
            final int prePos = mSelectPosition;
            mSelectPosition = position;
            if (prePos != NO_POSITION) {
                unselects = new int[]{prePos};
            }
            selects = new int[]{position};
        }
        notifySelectorState(unselects, selects);
    }

    @Override
    public void clearSelectedPosition() {
        mSelectPosition = NO_POSITION;
    }

    @Override
    public void clearSelectedState() {
        if (mSelectPosition != NO_POSITION) {
            final int prePos = mSelectPosition;
            mSelectPosition = NO_POSITION;
            notifySelectorState(new int[]{prePos}, null);
        }
    }

    @Override
    public int[] getSelectPosition() {
        if (mSelectPosition == NO_POSITION) {
            return null;
        }
        return new int[]{mSelectPosition};
    }
}
