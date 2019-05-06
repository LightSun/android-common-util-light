package com.heaven7.adapter;

import android.content.Context;

import com.heaven7.adapter.drag.ItemTouchAdapterDelegate;
import com.heaven7.adapter.util.ViewHelper2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2019/3/5.
 * @since 2.0.0
 */
public final class RecycleViewComponentsAdapter<T extends ISelectable> extends QuickRecycleViewAdapter<T> implements ItemTouchAdapterDelegate {

    private final List<AdapterApplier<? super T>> appliers = new ArrayList<>();
    private Object mAdapterContextData;

    public RecycleViewComponentsAdapter(int layoutId, List<T> mDatas) {
        super(layoutId, mDatas);
    }

    public RecycleViewComponentsAdapter(int layoutId, List<T> mDatas, int selectMode) {
        super(layoutId, mDatas, selectMode);
    }

    public RecycleViewComponentsAdapter<T> addAdapterApplier(AdapterApplier<? super T> applier){
        appliers.add(applier);
        return this;
    }
    public RecycleViewComponentsAdapter<T> addAdapterAppliers(List<AdapterApplier<? super T>> appliers){
        this.appliers.addAll(appliers);
        return this;
    }

    public RecycleViewComponentsAdapter<T> setContextData(Object data){
        this.mAdapterContextData = data;
        return this;
    }
    public Object getContextData() {
        return mAdapterContextData;
    }

    public void delete(int position) {
        getAdapterManager().removeItem(position);
    }

    @Override
    public void move(int from, int to) {
        AdapterManager<T> am = getAdapterManager();
        T prev = am.getItemAt(from);

        List<T> items = am.getItems();
        items.remove(from);
        items.add(to > from ? to - 1 : to, prev);
        getAdapterManager().notifyItemMoved(from, to);
    }

    @Override
    protected void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper2 helper) {
        Object data = this.mAdapterContextData;
        for (AdapterApplier<? super T> applier : appliers){
            applier.apply(context, data,  position, item, itemLayoutId, helper);
        }
    }

    public interface AdapterApplier<T extends ISelectable>{
        void apply(Context context, Object contextData, int position, T item, int itemLayoutId, ViewHelper2 helper);
    }

    /**
     * just mark applier support two state.
     */
    public interface TwoStateApplier{

    }

    public static class Builder<T extends ISelectable>{
        private List<AdapterApplier<? super T>> appliers = new ArrayList<>();
        private Object contextData;
        private int layoutId;
        private List<T> datas;
        private int selectMode = ISelectable.SELECT_MODE_SINGLE;

        public Builder<T> setContextData(Object mAdapterContextData) {
            this.contextData = mAdapterContextData;
            return this;
        }
        public Builder<T> setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }
        public Builder<T> setSelectMode(int selectMode) {
            this.selectMode = selectMode;
            return this;
        }
        public Builder<T> addAdapterApplier(AdapterApplier< ? super T> applier){
            appliers.add(applier);
            return this;
        }
        public Builder<T> setData(List<T> datas) {
            this.datas = datas;
            return this;
        }
        public RecycleViewComponentsAdapter<T> build(){
            if(appliers == null || appliers.isEmpty()){
                throw new IllegalStateException("appliers can't be empty");
            }
            RecycleViewComponentsAdapter<T> adapter = new RecycleViewComponentsAdapter<>(layoutId, datas, selectMode);
            adapter.addAdapterAppliers(appliers);
            adapter.setContextData(contextData);
            return adapter;
        }
    }
}
