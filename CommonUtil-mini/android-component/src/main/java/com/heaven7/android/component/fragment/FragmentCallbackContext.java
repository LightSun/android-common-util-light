package com.heaven7.android.component.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.heaven7.core.util.ViewHelper;

/**
 * Created by heaven7 on 2016/5/24.
 */
public final class FragmentCallbackContext {

    private FragmentMediator mMediator;

    /*public*/ void setFragmentMediator(FragmentMediator mediator){
        this.mMediator = mediator;
    }
    public FragmentMediator getFragmentMediator(){
        return mMediator;
    }

    public Fragment getFragment(){
        return  mMediator.getFragment();
    }
    public ViewHelper getViewHelper(){
        return mMediator.getViewHelper();
    }
    public Bundle getSaveBundle(){
        return mMediator.getSaveBundle();
    }
}
