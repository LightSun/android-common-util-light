package com.heaven7.android.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

/**
 * the message server
 * Created by heaven7 on 2016/7/1.
 */
public abstract class MessageServer extends RemoteMessageContext{

    private ServiceConnection mConn;
    private IRemoteServerManager mServerManager;

    private final IRemoteServerCallback mCallback = new IRemoteServerCallback.Stub(){
        @Override
        public Message processMessage(int policy, Message msg) throws RemoteException {
            return MessageServer.this.processMessage(policy, msg);
        }
    };

    public MessageServer(Context context) {
        super(context);
    }

    /**
     *  process the target message to a new message, or null if ignore this message.
     *  this method is called in sub thread.
     * @param policy  the message policy ,see {@link MessageService#POLICY_BROADCAST} and etc.
     * @param msg the  source message
     * @return a new message to send, or null if ignore this message
     */
    protected abstract Message processMessage(int policy, Message msg);

    @Override
    public void sendMessage(Message msg, @MessageService.MessagePolicy int type) {
        if(type == MessageService.POLICY_REPLY){
            throw new IllegalArgumentException("message server don't support this type('POLICY_REPLY').");
        }
        super.sendMessage(msg, type);
    }

    @Override
    protected void bindImpl() {
        getContext().bindService(new Intent(getContext(),MessageService.class)
                        .setAction(MessageService.ACTION_SERVER_MANAGER),
                mConn = new ServerCallbackConnectionImpl(), Context.BIND_AUTO_CREATE );
        super.bindImpl();
    }

    @Override
    protected void unbindImpl() {
        if(mServerManager!=null){
            try {
                mServerManager.setRemoteServerCallback(null);
            } catch (RemoteException e) {
                // There is nothing special we need to do if the service
                // has crashed.
            }
        }
        if(mConn != null) {
            getContext().unbindService(mConn);
            mConn = null;
        }
        super.unbindImpl();
    }

    private class ServerCallbackConnectionImpl implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServerManager = IRemoteServerManager.Stub.asInterface(service);
            try {
                mServerManager.setRemoteServerCallback(mCallback);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServerManager = null;
        }
    }
}
