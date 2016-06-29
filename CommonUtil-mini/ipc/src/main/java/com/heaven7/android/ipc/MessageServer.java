package com.heaven7.android.ipc;

import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

/**
 * Created by heaven7 on 2016/6/29.
 */
public class MessageServer {

    public static final String INTENT_DEFAULT = MessageServer.class.getName();

    private final RemoteCallbackList<IRemoteServiceCallback> mCallbacks
            = new RemoteCallbackList<IRemoteServiceCallback>();

    private final InternalHandler mHandler = new InternalHandler(this);
    /**
     * The IRemoteInterface is defined through AIDL
     */
    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        public void registerCallback(IRemoteServiceCallback cb) {
            if (cb != null) mCallbacks.register(cb);
        }
        public void unregisterCallback(IRemoteServiceCallback cb) {
            if (cb != null) mCallbacks.unregister(cb);
        }

        @Override
        public void sendMessage(Message msg) throws RemoteException {
            mHandler.sendMessage(msg);
        }
    };
    //mCallbacks.kill();

    public IBinder getBinder(){
        return mBinder;
    }
    private static class InternalHandler extends WeakHandler<MessageServer>{

        public InternalHandler(MessageServer messageServer) {
            super(messageServer);
        }
        public InternalHandler(Looper looper, MessageServer messageServer) {
            super(looper, messageServer);
        }

        @Override
        public void handleMessage(Message msg) {
            // Broadcast to all clients the new value.
            final MessageServer server = get();
            final int N = server.mCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    server.mCallbacks.getBroadcastItem(i).onMessageReceived(msg);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            server.mCallbacks.finishBroadcast();
        }
    }

}
