package com.heaven7.android.mini.demo;

import com.heaven7.android.mini.demo.sample.FragmentComponentTestActivity;
import com.heaven7.android.mini.demo.sample.IpcTestActivity;
import com.heaven7.android.mini.demo.sample.LoaderMvpTest;
import com.heaven7.android.mini.demo.sample.RecyclerViewNestedTest;

import java.util.List;

/**
 * Created by heaven7 on 2016/5/25.
 */
public class MainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(FragmentComponentTestActivity.class, "FragmentComponentTestActivity"));
       // list.add(new ActivityInfo(LogTestActivity.class, "LogTestActivity"));
        list.add(new ActivityInfo(IpcTestActivity.class, "IpcTestActivity"));
        list.add(new ActivityInfo(RecyclerViewNestedTest.class, "RecyclerViewNestedTest"));
        list.add(new ActivityInfo(LoaderMvpTest.class, "LoaderMvpTest"));
    }
}
