package com.heaven7.android.ipc;

import com.heaven7.android.ipc.IRemoteServerCallback;

interface IRemoteServerManager {

       /**
        * set the remote server callback
        * @param callback the server callback
        * @deprecated
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
}
