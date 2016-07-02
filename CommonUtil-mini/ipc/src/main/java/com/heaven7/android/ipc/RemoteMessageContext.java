package com.heaven7.android.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * the remote message context
 * Created by heaven7 on 2016/7/1.
 */
public abstract class RemoteMessageContext {

    private final Context mContext;
    private boolean mIsBound;

    /** the message service Messenger */
    private Messenger mMessageService;
    private MessageServiceConnectionImpl mMessageServiceConn;

    public RemoteMessageContext(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public Context getContext(){
        return mContext;
    }

    /**
     * as clien: send a message to server. and the server will process the message.
     * as server: send a message to clients
     * @param msg the message to send
     * @param type the type of message response ,see {@link MessageService#POLICY_BROADCAST} and etc.
     */
    public void sendMessage(Message msg , @MessageService.MessagePolicy int type){
        msg.arg2 = type;
        if(type == MessageService.POLICY_REPLY){
            msg.replyTo = getClientMessager();
        }
        try {
            //msg.setAsynchronous();
            mMessageService.send(msg);
        } catch (RemoteException e) {
            //just ignore
        }
    }

    /** get the client messager,server don't need  */
    protected Messenger getClientMessager(){
        return null;
    }

    /** bind service */
    public final void bind(){
        if(mIsBound){
            return;
        }
        bindImpl();
        mIsBound = true;
    }

    /** unbind service */
    public final void unbind(){
        if(!mIsBound){
            return;
        }
        unbindImpl();
        mIsBound = false;
    }

    /** called in the {@link #bind()}  */
    protected void bindImpl(){
        getContext().bindService(new Intent(MessageService.ACTION_MESSAGE_SERVICE),
                mMessageServiceConn = new MessageServiceConnectionImpl(), Context.BIND_AUTO_CREATE);
    }

    /** called in the {@link #unbind()}  */
    protected  void unbindImpl(){
        // Detach our existing connection.
        if(mMessageServiceConn!=null) {
            getContext().unbindService(mMessageServiceConn);
            mMessageServiceConn = null;
        }
    }

    /**
     * called when this client is connected.
     */
    protected void afterConnected(){

    }
    /**
     * called when this client is disconnected.
     */
    protected void afterDisconnected(){

    }

    /**
     * the message service ServiceConnection implement
     */
    private class MessageServiceConnectionImpl implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessageService = new Messenger(service);
            afterConnected();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessageService = null;
            afterDisconnected();
        }
    }
}
