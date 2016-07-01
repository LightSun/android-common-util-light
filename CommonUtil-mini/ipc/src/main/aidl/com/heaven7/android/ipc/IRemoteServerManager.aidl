package com.heaven7.android.ipc;

import com.heaven7.android.ipc.IRemoteServerCallback;

interface IRemoteServerManager {
    /**
     * set the remote server callback
     */
     void setRemoteServerCallback(IRemoteServerCallback callback);
}
