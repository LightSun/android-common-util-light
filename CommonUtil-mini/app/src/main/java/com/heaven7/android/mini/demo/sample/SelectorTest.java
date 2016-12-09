package com.heaven7.android.mini.demo.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by heaven7 on 2016/12/5.
 */
public class SelectorTest extends BaseActivity {

    @InjectView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getlayoutId() {
        return R.layout.ac_recycler_view_nested;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> adapter = new QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData>(
                android.R.layout.simple_list_item_1, createTest()) {
            @Override
            protected void onBindData(Context context, int position, RecyclerViewNestedTest.ChildData item, int itemLayoutId, ViewHelper helper) {
                helper.setTextColor(android.R.id.text1, item.isSelected() ? Color.RED : Color.BLACK)
                   .setText(android.R.id.text1, item.text);
                        //.setOnClickListener(new )
            }
        };
        mRv.setAdapter(adapter);
    }

    private List<RecyclerViewNestedTest.ChildData> createTest() {
        List<RecyclerViewNestedTest.ChildData> list = new ArrayList<>();
        for(int i = 0 ;  i < 50 ; i++){
            final RecyclerViewNestedTest.ChildData data = new RecyclerViewNestedTest.ChildData("SelectorTest___" + i);
            list.add(data);
        }
        return list;
    }



}
