package com.heaven7.android.component.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by heaven7 on 2016/5/24.
 */
public final class FragmentFactory {

       public  static  Fragment newFragment(FragmentHelper helper){
           BaseFragment fragment = new BaseFragment();
           final FragmentMediator mediator = fragment.getFragmentMediator();
           mediator.init(helper.getLayoutId(), helper.getArguments());
           mediator.getFragmentCallbacks().addFragmentCallbacks(helper.getFragmentCallbacks());
           if(helper.getMenuCallback()!=null) {
               mediator.setMenuCallback(helper.getMenuCallback());
           }
           return  fragment;
       }
}
