package com.heaven7.android.ipc;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by heaven7 on 2016/7/15.
 */
public class IpcConstant {

    public static final String ACTION_CLIENT_MANAGER   = "com.heaven7.android.ipc.client_manager";
    public static final String ACTION_MESSAGE_SERVICE  = "com.heaven7.android.ipc.message_service";
    public static final String ACTION_SERVER_MANAGER   = "com.heaven7.android.ipc.server_manager";

    /** this indicate the message is a broadcast message .*/
    public static final int POLICY_BROADCAST    =  0x0001 ;
    /** this indicate the message is a 'consume-mode' message. */
    public static final int POLICY_CONSUME      =  0x0002 ;
    /** this indicate the message is a 'need-reply' message .*/
    public static final int POLICY_REPLY        =  0x0004 ;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POLICY_BROADCAST, POLICY_CONSUME, POLICY_REPLY})
    public @interface MessagePolicy {

    }
}
