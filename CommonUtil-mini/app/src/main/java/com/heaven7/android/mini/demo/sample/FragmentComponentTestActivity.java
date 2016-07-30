package com.heaven7.android.mini.demo.sample;

import android.os.Bundle;
import android.view.View;

import com.heaven7.android.component.fragment.FragmentFactory;
import com.heaven7.android.component.fragment.FragmentHelper;
import com.heaven7.android.component.fragment.IDataBinder;
import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.BundleHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.Toaster;
import com.heaven7.core.util.ViewHelper;

import java.lang.reflect.Field;

/**
 * Created by heaven7 on 2016/5/25.
 */
public class FragmentComponentTestActivity extends BaseActivity {

    private final IDataBinder mDataBinder = new IDataBinder() {
        @Override
        public void onBindData(ViewHelper view, Bundle arguments, Bundle saveBundle) {
            view.setText(R.id.tv_title, arguments.getString("title"))
                    .setRootOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toaster.show(v.getContext(),"onClick");
                        }
                    });
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.ac_frag_component_test;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        replaceFragment(R.id.fl, FragmentFactory.newFragment(new FragmentHelper(R.layout.frag_component_test,
                        new BundleHelper()
                                .putString("title", "heaven7")
                                .getBundle())
                .setDataBinder(mDataBinder)
        ),false);

        try {
            Field f = Class.forName("sun.misc.Unsafe").getDeclaredField("THE_ONE");
            f.setAccessible(true);
            Object unsafe = f.get(null);
            Logger.i("FragmentComponentTestActivity", "test unsafe: " + unsafe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
