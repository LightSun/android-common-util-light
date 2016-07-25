package com.heaven7.android.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * the message client
 * Created by heaven7 on 2016/6/29.
 */
public class MessageClient extends RemoteMessageContext{

    private ServiceConnection mClientManagerConn;

    private IRemoteClientManager mClientManager;
    private Messenger mClient ;

    /**
     * the remote client callback
     */
    private final IRemoteClientCallback mCallback = new IRemoteClientCallback.Stub(){
        @Override
        public void onReceive(Message msg) throws RemoteException {
            MessageClient.this.onReceive(msg);
        }
        @Override
        public boolean consumeMessage(Message msg) throws RemoteException {
            return MessageClient.this.consumeMessage(msg);
        }
    };

    public MessageClient(Context context) {
        super(context);
        this.mClient = new Messenger(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                MessageClient.this.handleReplyMessage(msg);
            }
        });
    }

    @Override
    protected void bindImpl() {
        getContext().bindService(createServiceIntent(new Intent(IpcConstant.ACTION_CLIENT_MANAGER)),
                mClientManagerConn = new ClientManagerConnectionImpl(), Context.BIND_AUTO_CREATE);
        super.bindImpl();
    }

    @Override
    protected void unbindImpl() {
        // If we have received the service, and hence registered with
        // it, then now is the time to unregister.
        if (mClientManager != null) {
            try {
                mClientManager.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                // There is nothing special we need to do if the service
                // has crashed.
            }
        }
        // Detach our existing connection.
        if(mClientManagerConn!=null) {
            getContext().unbindService(mClientManagerConn);
            mClientManagerConn = null;
        }
        super.unbindImpl();
    }

    @Override
    protected Messenger getClientMessager() {
        return mClient;
    }

    /**
     *  called when client receive a broadcast message from server
     * @param msg the new message
     */
    protected void onReceive(Message msg){

    }
    /**
     * @param msg the new message
     * @return true if you handle it .
     */
    protected boolean consumeMessage(Message msg){
        return false;
    }
    /**
     * called when server reply message to the message requester.
     * @param msg the new message
     */
    protected void handleReplyMessage(Message msg){

    }
    private class ClientManagerConnectionImpl implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mClientManager = IRemoteClientManager.Stub.asInterface(service);
            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                mClientManager.registerCallback(mCallback);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mClientManager = null;
        }
    }

}
