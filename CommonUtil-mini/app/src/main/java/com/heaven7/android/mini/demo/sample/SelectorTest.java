package com.heaven7.android.mini.demo.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.ISelectable;
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

        QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> adapter = createTestSelectAdapter();

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);
    }

    private QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> createTestSelectAdapter() {
        return new TestSelectAdapter(ISelectable.SELECT_MODE_MULTI);
    }

    @NonNull
    private QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> createTestDeleteAdapter() {
        return new TestDeleteAdapter();
    }

    private List<RecyclerViewNestedTest.ChildData> createTest(int count) {
        List<RecyclerViewNestedTest.ChildData> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final RecyclerViewNestedTest.ChildData data = new RecyclerViewNestedTest.ChildData("SelectorTest___" + i);
            list.add(data);
        }
        last = count - 1;
        return list;
    }

    private RecyclerViewNestedTest.ChildData newData() {
        return new RecyclerViewNestedTest.ChildData("SelectorTest___" +
                (++last));
    }

    /**
     * 选择状态问题，测试删除，添加对选择状态的影响.
     */
    class TestSelectAdapter extends QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> {

        private boolean selectMode = true;

        public TestSelectAdapter() {
            this(ISelectable.SELECT_MODE_SINGLE);
        }

        public TestSelectAdapter(int selectMode) {
            super(android.R.layout.simple_list_item_1, createTest(50), selectMode);
        }

        @Override //tested in 1.8.5
        protected void onBindData(Context context, final int position, RecyclerViewNestedTest.ChildData item,
                                  int itemLayoutId, final ViewHelper helper) {
            Logger.w("SelectorTest", "onBindData", "position = " + position + " ," + item.text);
            //点击 切换select状态， 长按replaceAllItems
            helper.view(android.R.id.text1)
                    .setTextColor(item.isSelected() ? Color.RED : Color.BLACK)
                    .setText(item.text)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selectMode) {
                                getSelectHelper().toggleSelect(position);
                            } else {
                                getAdapterManager().removeItemForRecyclerView(helper.getRootView());
                            }
                            // getAdapterManager().replaceAllItems(createTest(5));
                        }
                    }).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // getAdapterManager().removeItemForRecyclerView(helper.getRootView()); //ok
                    // getAdapterManager().addItem(1, newData()); // OK
                    selectMode = !selectMode;
                    showToast("已切换模式为: " + (selectMode ? "select mode" : "delete mode"));
                    return true;
                }
            });
        }
    }

    class TestDeleteAdapter extends QuickRecycleViewAdapter<RecyclerViewNestedTest.ChildData> {

        public TestDeleteAdapter() {
            this(ISelectable.SELECT_MODE_SINGLE);
        }

        public TestDeleteAdapter(int selectMode) {
            super(android.R.layout.simple_list_item_1, createTest(50), selectMode);
        }

        @Override
        protected void onBindData(Context context, final int position, RecyclerViewNestedTest.ChildData item,
                                  int itemLayoutId, final ViewHelper helper) {
            Logger.w("SelectorTest", "onBindData", "position = " + position + " ," + item.text);
            helper.setTextColor(android.R.id.text1, item.isSelected() ? Color.RED : Color.BLACK)
                    .setText(android.R.id.text1, item.text)
                    .setRootOnClickListener(new TestDeleteOnClickListener(getAdapterManager(), helper));
        }
    }

    /**
     * 测试删除问题。, bug已修复.
     */
    private class TestDeleteOnClickListener implements View.OnClickListener {
        final ViewHelper helper;
        final AdapterManager am;

        public TestDeleteOnClickListener(AdapterManager am, ViewHelper helper) {
            this.am = am;
            this.helper = helper;
        }

        @Override
        public void onClick(View v) {
            //这里不能直接用position.否则多次删除会有问题-bug.
            final int pos = mRv.getChildAdapterPosition(v);
            final int layoutPos = mRv.getChildViewHolder(v).getLayoutPosition();
            Logger.w("SelectorTest", "onClick", "getAdapterPosition() = " + pos + " ,layoutPos = " + layoutPos);
            // getAdapterManager().removeItem(pos); //ok
            am.removeItemForRecyclerView(helper.getRootView()); //ok
        }
    }


}
