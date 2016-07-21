package com.heaven7.android.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.heaven7.android.ipc.IRemoteClientCallback;
import com.heaven7.android.ipc.IRemoteClientManager;
import com.heaven7.android.ipc.IRemoteServerCallback;
import com.heaven7.android.ipc.IRemoteServerManager;
import com.heaven7.android.ipc.IpcConstant;

/**
 * the service of background
 * Created by heaven7 on 2016/6/29.
 */
public class MessageService extends Service{

    private static final String TAG = "MessageService";

    private final RemoteCallbackList<IRemoteClientCallback> mClientCallbacks
            = new RemoteCallbackList<IRemoteClientCallback>();
    private final RemoteCallbackList<IRemoteServerCallback> mServerCallbacks
            = new RemoteCallbackList<IRemoteServerCallback>();

    private final InternalHandler mHandler = new InternalHandler(this);
    private volatile Looper mServiceLooper;
    private Messenger mMessenger;

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
            registerRemoteServerCallback(callback);
        }

        @Override
        public void registerRemoteServerCallback(IRemoteServerCallback callback) throws RemoteException {
            /**
             * current server only support one server.
             */
            if( !isServerCallbackListEmpty() ){
                Log.i(TAG,"registerRemoteServerCallback() failed. because by had register a callback.");
            }else {
                mServerCallbacks.register(callback);
            }
        }

        @Override
        public void unregisterRemoteServerCallback(IRemoteServerCallback callback) throws RemoteException {
            mServerCallbacks.unregister(callback);
        }
    };

    private boolean isServerCallbackListEmpty(){
        if(Build.VERSION.SDK_INT >= 17 ){
            return mServerCallbacks.getRegisteredCallbackCount() == 0 ;
        }else{
            int size = mServerCallbacks.beginBroadcast();
            mServerCallbacks.finishBroadcast();
            return size == 0;
        }
    }
    private IRemoteServerCallback getDefaultServerCallback(){
        IRemoteServerCallback callback = null;
        int size = mServerCallbacks.beginBroadcast();
        if(size > 0){
            callback = mServerCallbacks.getBroadcastItem(0);
        }
        mServerCallbacks.finishBroadcast();
        return callback;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final String action = intent.getAction();
        if(IpcConstant.ACTION_CLIENT_MANAGER.equals(action)){
            return mClientManagerBinder;
        }else if(IpcConstant.ACTION_MESSAGE_SERVICE.equals(action)){
            return mMessenger.getBinder();
        }else if(IpcConstant.ACTION_SERVER_MANAGER.equals(action)){
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
                IRemoteServerCallback callback ;
                if( (callback = getDefaultServerCallback())==null){
                    Log.w(TAG, "called [ handleMessage() ]: but there is no server to process(convert) message.");
                    return;
                }
                try {
                    Message newMsg = callback.processMessage(msg.arg2, msg);
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
        mServerCallbacks.kill();
        mServiceLooper.quit();
        super.onDestroy();
    }

    private static class InternalHandler extends WeakHandler<MessageService> {

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
                case IpcConstant.POLICY_CONSUME:
                    handleConsumeMessage(service, msg);
                    break;

                case IpcConstant.POLICY_REPLY:
                    handleReplyMessage(msg.replyTo, msg);
                    break;

                default:
                case IpcConstant.POLICY_BROADCAST:
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

        private void handleBroadcastMessage(MessageService service, Message msg) {
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
