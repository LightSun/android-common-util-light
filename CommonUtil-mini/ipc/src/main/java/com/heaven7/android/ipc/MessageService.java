package com.heaven7.android.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
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
 * the service of background
 * Created by heaven7 on 2016/6/29.
 */
public class MessageService extends Service{

    private static final String TAG = "MessageService";

    public static final String ACTION_CLIENT_MANAGER   = "com.heaven7.android.ipc.client_manager";
    public static final String ACTION_MESSAGE_SERVICE  = "com.heaven7.android.ipc.message_service";
    public static final String ACTION_SERVER_MANAGER   = "com.heaven7.android.ipc.server_manager";

    /** this indicate the message is a broadcast message .*/
    public static final int POLICY_BROADCAST    =  0x0001 ;
    /** this indicate the message is a 'consume-mode' message. */
    public static final int POLICY_CONSUME      =  0x0002 ;
    /** this indicate the message is a 'need-reply' message .*/
    public static final int POLICY_REPLY        =  0x0003 ;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POLICY_BROADCAST, POLICY_CONSUME, POLICY_REPLY})
    public @interface MessagePolicy {

    }

    private final RemoteCallbackList<IRemoteClientCallback> mClientCallbacks
            = new RemoteCallbackList<IRemoteClientCallback>();

    private final InternalHandler mHandler = new InternalHandler(this);
    private volatile Looper mServiceLooper;
    private Messenger mMessenger;

    private volatile IRemoteServerCallback mServerCallback;
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
        if(ACTION_CLIENT_MANAGER.equals(action)){
            return mClientManagerBinder;
        }else if(ACTION_MESSAGE_SERVICE.equals(action)){
            return mMessenger.getBinder();
        }else if(ACTION_SERVER_MANAGER.equals(action)){
            return mServerManagerBinder;
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("MessageService");
        thread.start();

        mServiceLooper = thread.getLooper();
        mMessenger = new Messenger(new Handler(mServiceLooper){
            @Override
            public void handleMessage(Message msg) {
                if(mServerCallback == null){
                    Log.w(TAG, "called [ handleMessage() ]: but there is no server to process(convert) message.");
                    return;
                }
                try {
                    Message newMsg = mServerCallback.processMessage(msg.arg2, msg);
                    if(newMsg == null){
                        //ignore this message
                        return;
                    }
                    if(newMsg != msg) {
                        newMsg.arg2 = msg.arg2;
                        newMsg.replyTo = msg.replyTo;
                    }
                    mHandler.sendMessage(newMsg);
                } catch (RemoteException e) {
                   //ignore
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mClientCallbacks.kill();
        mServiceLooper.quit();
        super.onDestroy();
    }

    private static class InternalHandler extends WeakHandler<MessageService>{

        public InternalHandler(MessageService messageServer) {
            super(messageServer);
        }
        @Override
        public void handleMessage(Message msg) {
            final MessageService service = get();
            /**
             * 1, broadcast message
             * 2, consume
             * 3, reply
             */
            switch (msg.arg2){
                case POLICY_CONSUME:
                    handleConsumeMessage(service, msg);
                    break;

                case POLICY_REPLY:
                    handleReplyMessage(msg.replyTo, msg);
                    break;

                default:
                case POLICY_BROADCAST:
                    handleBroadcastMessage(service, msg);
                    break;
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
