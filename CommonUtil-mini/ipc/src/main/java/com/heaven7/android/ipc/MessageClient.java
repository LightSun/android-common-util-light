package com.heaven7.android.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

/**
 * Created by heaven7 on 2016/6/29.
 */
public class MessageClient {

    private final Context mContext;
    private ServiceConnection mConn;
    private IRemoteService mService = null;

    private boolean mIsBound;
    private final IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub(){
        @Override
        public void onMessageReceived(Message msg) throws RemoteException {

        }
    };

    public MessageClient(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void bind(Intent service){
        mContext.bindService(service, mConn = new ServiceConnectionImpl(), Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    public void unbind(){
        if(!mIsBound){
            return;
        }
        // If we have received the service, and hence registered with
        // it, then now is the time to unregister.
        if (mService != null) {
            try {
                mService.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                // There is nothing special we need to do if the service
                // has crashed.
            }
        }
        // Detach our existing connection.
        if(mConn != null){
            mContext.unbindService(mConn);
        }
        mIsBound = false;
    }

    public void sendMessageToServer(Bundle data){
        //mService.sendMessage(Message.obtain());
    }

    protected void afterConnected(Context context){

    }
    protected void afterDisconnected(Context context){
       // RemoteCallbackList
    }

    private class ServiceConnectionImpl implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
             //TODO
            mService = IRemoteService.Stub.asInterface(service);
            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
            afterConnected(mContext);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            afterDisconnected(mContext);
        }
    }

}
