package com.heaven7.android.mini.demo.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.adapter.ResizeHeightPostCallback;
import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.viewhelper.action.Getters;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * test: recyclerView nested recycler view . all orientation is vertical
 * Created by heaven7 on 2016/12/5.
 */
public class RecyclerViewNestedTest extends BaseActivity {

    @InjectView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getlayoutId() {
        return R.layout.ac_recycler_view_nested;
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(this));
      //  mRv.setHasFixedSize(true);

        mRv.setAdapter(new QuickRecycleViewAdapter<ParentData>(R.layout.item_recycler_view, createParentList()) {

            @Override
            protected void onBindData(Context context, int position, final ParentData item, int itemLayoutId, ViewHelper helper) {
                helper.performViewGetter(R.id.rv_child, new Getters.RecyclerViewGetter() {
                    @Override
                    public void onGotView(RecyclerView view, ViewHelper vp) {
                       // view.setHasFixedSize(true);
                        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        setChildAdapter(view, item);
                    }
                });
            }
        });
    }
    //child recycler view heigh init mast be as large as.
    private void setChildAdapter(RecyclerView view, ParentData item) {
        QuickRecycleViewAdapter<ChildData> adapter = new QuickRecycleViewAdapter<ChildData>(android.R.layout.simple_list_item_1, item.mChildren) {
            @Override
            protected void onBindData(Context context, int position, ChildData item, int itemLayoutId, ViewHelper helper) {
                helper.setText(android.R.id.text1, item.text);
            }
        };
        adapter.getAdapterManager().addPostRunnableCallback(new ResizeHeightPostCallback<ChildData>(view));
        view.setAdapter(adapter);
    }

    public static List<ParentData> createParentList(){
        List<ParentData>  list = new ArrayList<>();

        for(int i = 0 ; i< 5  ;i ++){
            ParentData data = new ParentData();
            data.add("[ ParentData_"+ i + " ]" , 50);
            list.add(data);
        }
        return list;
    }


    public static class ParentData extends BaseSelector{

        public final List<ChildData> mChildren = new ArrayList<>();

        public void add(String tag, int count){
            for(int i = 0  ; i < count ; i++ ){
                mChildren.add(new ChildData(tag + "_child_" + i));
            }
        }
    }

    public static class ChildData extends BaseSelector{

        public String text;

        public ChildData(String text) {
            this.text = text;
        }
    }
}
