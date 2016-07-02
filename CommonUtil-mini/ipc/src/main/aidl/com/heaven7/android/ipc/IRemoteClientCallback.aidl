// IRemoteServiceCallback.aidl
package com.heaven7.android.ipc;

// Declare any non-default types here with import statements
import android.os.Message;

interface IRemoteClientCallback {

 /**
   * called when receive a broadcast message.
   * @param msg the message be received
   */
   void onReceive(in Message msg);
   /**
     * true to consume the message,false other wise
     * @param msg the source message
     * @return true if consume the target message
     */
   boolean consumeMessage(in Message msg);
}
