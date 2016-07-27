package com.heaven7.android.ipc;

import com.heaven7.android.ipc.IRemoteServerCallback;

interface IRemoteServerManager {

       /**
        * set the remote server callback
        * @param callback the server callback
        * @deprecated use #registerRemoteServerCallback(IRemoteServerCallback) instead
        */
      void setRemoteServerCallback(IRemoteServerCallback callback);

      /**
        * register the remote server callback
        * @param callback the server callback
        */
     void registerRemoteServerCallback(IRemoteServerCallback callback);

      /**
        * unregister the remote server callback
        * @param callback the server callback
        */
     void unregisterRemoteServerCallback(IRemoteServerCallback callback);

     /**
      * send message to clients directly
      * @param msg the message to send
      */
      boolean sendMessageDirectly(in Message msg);
}
