package com.heaven7.adapter;

/**
 * the select helper interface.
 * Created by heaven7 on 2017/1/9.
 * @since 1.8.5
 */
public interface ISelectHelper {

    int NO_POSITION = -1;

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

        /**
         * get the selected item by target position
         * @param position the position
         * @return the item
         */
        //Object getSelectedItemAtPosition(int position);
    }

}
