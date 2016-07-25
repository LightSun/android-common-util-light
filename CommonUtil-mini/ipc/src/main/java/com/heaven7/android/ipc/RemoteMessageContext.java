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

    private static final String SERVICE_NAME     = "com.heaven7.android.ipc.server.MessageService";

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

    /** return if client/server is bind to the remote service. */
    public boolean isBound(){
        return mIsBound;
    }

    /**
     * <p> as client: send a message to server. and the server will process the message(eg: send new message to clients ).</p>
     * as server: send a message to clients
     * @param msg the message to send
     * @param policy the policy of message ,see {@link IpcConstant#POLICY_BROADCAST} and etc.
     * @return true if send message success.
     */
    public boolean sendMessage(Message msg , @IpcConstant.MessagePolicy int policy){
        if(mMessageService == null){
            System.err.println("have not bound success, have you call 'bind()' or is the service '"+SERVICE_NAME+"' is running ?.");
            return false;
        }
        msg.arg2 = policy;
        if(policy == IpcConstant.POLICY_REPLY){
            msg.replyTo = getClientMessager();
        }
        try {
            //msg.setAsynchronous();
            mMessageService.send(msg);
        } catch (RemoteException e) {
            //just ignore
            return false;
        }
        return true;
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
        getContext().bindService(
                createServiceIntent().setAction(IpcConstant.ACTION_MESSAGE_SERVICE),
                mMessageServiceConn = new MessageServiceConnectionImpl(), Context.BIND_AUTO_CREATE);
    }

    protected Intent createServiceIntent() throws IpcException{
        try {
            return new Intent(getContext(), Class.forName(SERVICE_NAME));
        } catch (ClassNotFoundException e) {
            throw new IpcException( "can't find the class "+ SERVICE_NAME, e);
        }
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
     * called when this client is disconnected. this normally not called. it is called in special case.
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
    public static class IpcException extends RuntimeException {

        public IpcException() {
        }

        public IpcException(String detailMessage) {
            super(detailMessage);
        }

        public IpcException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public IpcException(Throwable throwable) {
            super(throwable);
        }
    }
}
