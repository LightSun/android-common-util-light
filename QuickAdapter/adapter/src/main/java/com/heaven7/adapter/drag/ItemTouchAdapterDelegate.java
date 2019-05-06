package com.heaven7.adapter.drag;

/**
 * start on 2019/5/6.
 * @author heaven7
 * @since 2.0.0
 */
public interface ItemTouchAdapterDelegate {

    /**
     * called on move target position item to another position
     * @param from the from position
     * @param to the to position
     */
    void move(int from, int to);
}
