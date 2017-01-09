package com.heaven7.android.mini.demo.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.Logger;
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

    int last;

    @Override
    protected int getlayoutId() {
        return R.layout.ac_recycler_view_nested;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> adapter = new QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData>(
                android.R.layout.simple_list_item_1, createTest()) {
            @Override
            protected void onBindData(Context context, final int position, RecyclerViewNestedTest.ChildData item,
                                      int itemLayoutId, final ViewHelper helper) {
                Logger.w("SelectorTest", "onBindData", "position = " + position + " ," + item.text);
                helper.setTextColor(android.R.id.text1, item.isSelected() ? Color.RED : Color.BLACK)
                        .setText(android.R.id.text1, item.text)
                        .setRootOnClickListener(new View.OnClickListener() {
                            @Override  //v must be the root view
                            public void onClick(View v) {
                                //这里不能直接用position.否则多次删除会有问题.
                                final int pos = mRv.getChildAdapterPosition(v);
                                final int layoutPos = mRv.getChildViewHolder(v).getLayoutPosition();
                                Logger.w("SelectorTest", "onClick", "getAdapterPosition() = " + pos + " ,layoutPos = " + layoutPos);
                               // getAdapterManager().removeItem(pos); //ok
                                getAdapterManager().removeItemForRecyclerView(helper); //ok
                            }
                        });
            }
        };

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);
    }

    private List<RecyclerViewNestedTest.ChildData> createTest() {
        List<RecyclerViewNestedTest.ChildData> list = new ArrayList<>();
        int count = 50;
        for (int i = 0; i < count; i++) {
            final RecyclerViewNestedTest.ChildData data = new RecyclerViewNestedTest.ChildData("SelectorTest___" + i);
            list.add(data);
        }
        last = count - 1;
        return list;
    }


}
