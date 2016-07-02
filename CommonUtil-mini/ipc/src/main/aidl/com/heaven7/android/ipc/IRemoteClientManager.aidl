// IRemoteService.aidl
package com.heaven7.android.ipc;

import com.heaven7.android.ipc.IRemoteClientCallback;
// Declare any non-default types here with import statements

interface IRemoteClientManager {
       /**
         * Often you want to allow a service to call back to its clients.
         * This shows how to do so, by registering a callback interface with
         * the service.
         * @param cb the client callback
         */
        void registerCallback(in IRemoteClientCallback cb);

        /**
         * Remove a previously registered callback interface.
         * @param cb the client callback
         */
        void unregisterCallback(in IRemoteClientCallback cb);
}
