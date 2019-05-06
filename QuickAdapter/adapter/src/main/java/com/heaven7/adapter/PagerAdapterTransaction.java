package com.heaven7.adapter;

import java.util.Comparator;
import java.util.List;

/**
 * the transaction of pager adapter(BaseFragmentPagerAdapter).
 * Created by heaven7 on 2016/10/10.
 * @since 1.6.3
 */
public abstract class PagerAdapterTransaction {

    /**
     * add a FragmentData
     *
     * @param data the fragment data
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction addFragmentData(BaseFragmentPagerAdapter.FragmentData data);

    /**
     * add a FragmentData
     *
     * @param position the position to add
     * @param data the fragment data
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction addFragmentData(int position, BaseFragmentPagerAdapter.FragmentData data);

    /**
     * add FragmentData list
     *
     * @param position the position to add
     * @param datas the fragment datas
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction addFragmentData(int position, List<BaseFragmentPagerAdapter.FragmentData> datas);

    /**
     * add FragmentData list
     *
     * @param datas the fragment datas
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction addFragmentData(List<BaseFragmentPagerAdapter.FragmentData> datas);


    /**
     * remove FragmentData
     *
     * @param data the fragment data to remove
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction removeFragmentData(BaseFragmentPagerAdapter.FragmentData data);

    /**
     * remove FragmentData
     *
     * @param datas the list fragment data .
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction removeFragmentData(List<BaseFragmentPagerAdapter.FragmentData> datas);

    /**
     * sort the all FragmentData
     *
     * @param comparator the comparator to sort,
     * @param performer the sort performer
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction performSort(Comparator<BaseFragmentPagerAdapter.FragmentData> comparator, ISortPerformer performer);

    /**
     * update the all FragmentData
     *
     * @param performer the update performer
     * @since 1.6.3
     */
    public abstract PagerAdapterTransaction performUpdate(IUpdatePerformer performer);


    /**
     * end the transaction. and notify data change.
     * @since 1.6.3
     */
    public abstract void commit();


    /**
     * the sort performer
     * @since 1.6.3
     */
    public interface ISortPerformer{

        /**
         * sort the all fragment data
         * @param adapter the pager adapter
         * @param datas the all datas
         * @param comparator the comparator
         * @since 1.6.3
         */
        void performSort(BaseFragmentPagerAdapter adapter, List<BaseFragmentPagerAdapter.FragmentData> datas,
                         Comparator<BaseFragmentPagerAdapter.FragmentData> comparator);
    }
    /**
     * the update performer
     * @since 1.6.3
     */
    public interface IUpdatePerformer{

        /**
         * update the all fragment data.
         * @param adapter the pager adapter
         * @param datas the all datas
         * @since 1.6.3
         */
        void performUpdate(BaseFragmentPagerAdapter adapter, List<BaseFragmentPagerAdapter.FragmentData> datas);
    }

}
