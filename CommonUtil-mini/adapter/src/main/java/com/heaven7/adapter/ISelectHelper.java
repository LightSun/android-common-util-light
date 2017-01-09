package com.heaven7.adapter;

import java.util.List;

/**
 * the select helper interface.
 * Created by heaven7 on 2017/1/9.
 * @since 1.8.5
 */
public interface ISelectHelper {

    int NO_POSITION = -1;

    /**
     * init the all select position.
     * @param positions the all select positions to init.
     */
    void initSelectPosition(List<Integer> positions, boolean notify);
    /**
     * select the target position of item.
     * @param position the position.
     */
    void select(int position);

    /**
     * unselect(cancel select) the target position of item.
     * @param position the position.
     */
    void unselect(int position);

    /**
     * toggle the select state of the target item
     * @param position the position which indicate the target item.
     */
    void toggleSelect(int position);

    /**
     * clear the select position/positions of this select helper, but not notify date changed.
     */
    void clearSelectedPosition();

    /**
     * clear the selected position/positions of this select helper, and notify date changed.
     */
    void clearSelectedState();

    /**
     * get the select position list.
     * @return the list which contains the all select position.
     */
    int[] getSelectPosition();



    /**
     * @since 1.8.5
     */
    interface SelectorNotifier {

        void begin();

        void end();
        /**
         *  only used for  RecycleViewAdapter
         * @param positions the position of item
         */
        void notifyItemSelected(int[] positions);

        void notifyItemUnselected(int[] positions);
    }

}
