package com.heaven7.android.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by heaven7 on 2016/6/29.
 */
public class MessageService extends Service{

    private static final String TAG = "MessageService";

    public static final String ACTION_CLIENT_MANAGER   = IRemoteClientManager.class.getName();
    public static final String ACTION_MESSAGE_SERVICE  = IRemoteClientCallback.class.getName();
    public static final String ACTION_SERVER_MANAGER   = IRemoteServerManager.class.getName();

    /** this indicate the message is a broadcast message .*/
    public static final int TYPE_BROADCAST  =  0x0001 ;
    /** this indicate the message is a 'consume-mode' message. */
    public static final int TYPE_CONSUME    =  0x0002 ;
    /** this indicate the message is a 'need-reply' message .*/
    public static final int TYPE_REPLY      =  0x0003 ;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ TYPE_BROADCAST, TYPE_CONSUME, TYPE_REPLY })
    public @interface MessageResponseType{

    }

    private final RemoteCallbackList<IRemoteClientCallback> mClientCallbacks
            = new RemoteCallbackList<IRemoteClientCallback>();

    private final InternalHandler mHandler = new InternalHandler(this);
    private final Messenger mMessenger = new Messenger(mHandler);
    private IRemoteServerCallback mServerCallback;
    /**
     * The IRemoteInterface is defined through AIDL
     */
    private final IRemoteClientManager.Stub mClientManagerBinder = new IRemoteClientManager.Stub() {
        public void registerCallback(IRemoteClientCallback cb) {
            if (cb != null) mClientCallbacks.register(cb);
        }
        public void unregisterCallback(IRemoteClientCallback cb) {
            if (cb != null) mClientCallbacks.unregister(cb);
        }
    };
    private final IRemoteServerManager.Stub mServerManagerBinder = new IRemoteServerManager.Stub(){
        @Override
        public void setRemoteServerCallback(IRemoteServerCallback callback) throws RemoteException {
             mServerCallback = callback;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final String action = intent.getAction();
        if(action.equals(ACTION_CLIENT_MANAGER)){
            return mClientManagerBinder;
        }else if(action.equals(ACTION_MESSAGE_SERVICE)){
            return mMessenger.getBinder();
        }else if(action.equals(ACTION_SERVER_MANAGER)){
            return mServerManagerBinder;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        mClientCallbacks.kill();
        super.onDestroy();
    }

    private static class InternalHandler extends WeakHandler<MessageService>{

        public InternalHandler(MessageService messageServer) {
            super(messageServer);
        }
        @Override
        public void handleMessage(Message msg) {
            final MessageService service = get();
            if(service.mServerCallback == null){
                Log.w(TAG, "called [ handleMessage() ]: there is no server to process(convert) message.");
                return;
            }
            try {
                Message newMsg = service.mServerCallback.processMessage(msg.arg2, msg);
                /**
                 * 1, broadcast message
                 * 2, consume
                 * 3, reply
                 */
                switch (msg.arg2){
                    case TYPE_CONSUME:
                        handleConsumeMessage(service, newMsg);
                        break;

                    case TYPE_REPLY:
                        handleReplyMessage(msg.replyTo, newMsg);
                        break;

                    default:
                    case TYPE_BROADCAST:
                        handleBroadcastMessage(service, newMsg);
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void handleReplyMessage(Messenger replyTo, Message msg) {
            if(replyTo == null){
                throw new RuntimeException("must have a replier");
            }
            try {
                replyTo.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void handleConsumeMessage(MessageService service, Message msg) {
            final int N = service.mClientCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    if(service.mClientCallbacks.getBroadcastItem(i).consumeMessage(msg)){
                        break;
                    }
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            service.mClientCallbacks.finishBroadcast();
        }

        private void handleBroadcastMessage(MessageService service,Message msg) {
            final int N = service.mClientCallbacks.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    service.mClientCallbacks.getBroadcastItem(i).onReceive(msg);
                } catch (RemoteException e) {
                    // The RemoteCallbackList will take care of removing
                    // the dead object for us.
                }
            }
            service.mClientCallbacks.finishBroadcast();
        }
    }

}
