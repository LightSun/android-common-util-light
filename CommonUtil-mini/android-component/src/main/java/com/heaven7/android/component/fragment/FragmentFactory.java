package com.heaven7.android.component.fragment;

/**
 * Created by heaven7 on 2016/5/24.
 */
public final class FragmentFactory {

    public static BaseFragment newFragment(FragmentHelper helper) {
        return newFragment(helper, BaseFragment.class);
    }

    public static <T extends BaseFragment> T newFragment(FragmentHelper helper, Class<T> clazz) {
        T fragment;
        try {
            fragment = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final FragmentMediator mediator = fragment.getFragmentMediator();
        mediator.init(helper.getLayoutId(), helper.getArguments());
        mediator.getFragmentCallbacks().addFragmentCallbacks(helper.getFragmentCallbacks());
        if (helper.getMenuCallback() != null) {
            mediator.setMenuCallback(helper.getMenuCallback());
        }
        return fragment;
    }
}
