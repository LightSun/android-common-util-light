package com.heaven7.adapter.selector;

import com.heaven7.adapter.ISelectHelper;

/**
 * Created by heaven7 on 2017/1/9.
 */
public abstract class AbstractSelectHelper implements ISelectHelper {

    private final SelectorNotifier notifier;

    public AbstractSelectHelper(SelectorNotifier notifier) {
        this.notifier = notifier;
    }

    protected void notifyItemSelected(int[] positions) {
        notifier.notifyItemSelected(positions);
    }

    protected void notifyItemUnselected(int[] positions) {
        notifier.notifyItemUnselected(positions);
    }

    protected void beginNotify() {
        notifier.begin();
    }

    protected void endNotify() {
        notifier.end();
    }

    /**
     * notify the selector state.
     * @param unselectPostions the positions to cancel.
     * @param selectPostions the positions to join/add .
     */
    protected void notifySelectorState(int[] unselectPostions, int[] selectPostions){
        beginNotify();
        notifyItemUnselected(unselectPostions);
        notifyItemSelected(selectPostions);
        endNotify();
    }

    public static void checkPosition(int position){
        //TODO check ???
        // if(position)
    }
}
