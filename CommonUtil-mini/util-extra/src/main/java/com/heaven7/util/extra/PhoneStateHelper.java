package com.heaven7.util.extra;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by heaven7 on 2016/7/15.
 */
public class PhoneStateHelper {

    private final TelephonyManager mTm;
    private PhoneStateListener mListener;

    public PhoneStateHelper(Context context, PhoneStateListener l) {
        mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void registerPhoneStateListener(PhoneStateListener l, int events){
        mTm.listen(l, events);
        mListener = l;
    }

    public void unregister(){
        if(mListener!=null){
            mTm.listen(mListener ,PhoneStateListener.LISTEN_NONE);
            mListener = null;
        }
    }
}
