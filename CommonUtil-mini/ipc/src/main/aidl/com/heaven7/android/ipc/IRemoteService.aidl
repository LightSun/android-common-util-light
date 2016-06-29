// IRemoteService.aidl
package com.heaven7.android.ipc;

import com.heaven7.android.ipc.IRemoteServiceCallback;
// Declare any non-default types here with import statements

interface IRemoteService {
       /**
         * Often you want to allow a service to call back to its clients.
         * This shows how to do so, by registering a callback interface with
         * the service.
         */
        void registerCallback(in IRemoteServiceCallback cb);

        /**
         * Remove a previously registered callback interface.
         */
        void unregisterCallback(in IRemoteServiceCallback cb);

        /**
        * send a message to server
        */
        void sendMessage(in Message msg);
}
