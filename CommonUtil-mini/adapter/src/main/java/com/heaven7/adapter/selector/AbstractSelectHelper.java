package com.heaven7.adapter.selector;

import com.heaven7.adapter.ISelectHelper;

import java.util.List;

/**
 * Created by heaven7 on 2017/1/9.
 */
public abstract class AbstractSelectHelper implements ISelectHelper {

    private final SelectorNotifier notifier;

    public AbstractSelectHelper(SelectorNotifier notifier) {
        this.notifier = notifier;
    }

    /**
     * notify the selector state.
     * @param unselectPostions the positions to cancel.
     * @param selectPostions the positions to join/add .
     */
    @Override
    public void notifySelectorStateChanged(int[] unselectPostions, int[] selectPostions){
        notifier.notifySelectorStateChanged(unselectPostions, selectPostions);
    }

    protected void checkPosition(int position){
         if(position < 0){
             throw new IllegalArgumentException("position must > 0");
         }
    }

    /*protected*/ static int[] toIntArray(List<Integer> list){
        final int size = list.size();
        if (size == 0) {
            return null;
        }
        int[] posArr = new int[size];
        final Object[] arr = list.toArray();
        for (int i = size - 1; i >= 0; i--) {
            posArr[i] = Integer.valueOf(arr[i].toString());
        }
        return posArr;
    }
}
