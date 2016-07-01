// IRemoteServiceCallback.aidl
package com.heaven7.android.ipc;

// Declare any non-default types here with import statements
import android.os.Message;

interface IRemoteServiceCallback {

   void onMessageReceived(in Message msg);
}
