package com.heaven7.android.mini.demo.sample;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.heaven7.android.ipc.MessageClient;
import com.heaven7.android.ipc.MessageServer;
import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.BundleHelper;
import com.heaven7.core.util.Logger;

import java.util.Random;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ipc test
 * Created by heaven7 on 2016/7/2.
 */
public class IpcTestActivity extends BaseActivity {

    private static final String TAG = "IpcTestActivity";

    @InjectView(R.id.bt_bind_client)
    Button btBindClient;
    @InjectView(R.id.bt_bind_server)
    Button btBindServer;
    @InjectView(R.id.et_message)
    EditText etMessage;
    @InjectView(R.id.bt_send_by_client)
    Button btSendByClient;
    @InjectView(R.id.bt_send_by_server)
    Button btSendByServer;

    private final Random mRandom = new Random();
    private MessageClient mClient;
    private MessageServer mServer;

    @Override
    protected int getlayoutId() {
        return R.layout.ac_test_ipc;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mClient = new MessageClient(this){
            @Override
            protected void afterConnected() {
                showToast("client is connected.");
                Logger.i(TAG, "MessageClient_afterConnected", "client is connected.");
            }
            @Override
            protected void afterDisconnected() {
                showToast("client is disconnected.");
                Logger.i(TAG, "MessageClient_afterDisconnected", "client is disconnected.");
            }

            @Override
            protected void onReceive(Message msg) {
                Logger.i(TAG, "MessageClient_onReceive", toTestString(msg));
            }
            @Override
            protected boolean consumeMessage(Message msg) {
                Logger.i(TAG, "MessageClient_consumeMessage", toTestString(msg));
                return super.consumeMessage(msg);
            }
            @Override
            protected void handleReplyMessage(Message msg) {
                Logger.i(TAG, "MessageClient_handleReplyMessage", toTestString(msg));
            }
        };
        mServer = new MessageServer(this){
            @Override
            protected Message processMessage(int policy, Message msg) {
                msg.getData().putString("processor","MessageServer");
                return msg;
            }
            @Override
            protected void afterConnected() {
                showToast("server is connected.");
                Logger.i(TAG, "MessageServer_afterConnected", "server is connected.");
            }
            @Override
            protected void afterDisconnected() {
                showToast("server is disconnected.");
                Logger.i(TAG, "MessageServer_afterDisconnected", "server is disconnected.");
            }
        };
    }

    private String toTestString(Message msg) {
        return "msg = " + msg.getData().getString("msg") +
                " , processor = " + msg.getData().getString("processor");
    }

    @OnClick(R.id.bt_bind_client)
    public void onClickBindClient(View v){
        mClient.bind();
    }
    @OnClick(R.id.bt_unbind_client)
    public void onClickUnbindClient(View v){
        mClient.unbind();
        Logger.i(TAG , "onClickUnbindClient", "client is unbind now.");
    }
    @OnClick(R.id.bt_bind_server)
    public void onClickBindServer(View v){
        mServer.bind();
    }
    @OnClick(R.id.bt_unbind_server)
    public void onClickUnbindServer(View v){
        mServer.unbind();
        Logger.i(TAG , "onClickBindServer", "server is unbind now.");
    }
    @OnClick(R.id.bt_send_by_client)
    public void onClickSendMsgByClient(View v){
        final Message msg = Message.obtain();
        msg.setData(new BundleHelper().putString("msg", etMessage.getText().toString())
                .getBundle());
        msg.what = WHAT_MSG;
        mClient.sendMessage(msg, mRandom.nextInt() % 3 +1);
    }
    @OnClick(R.id.bt_send_by_server)
    public void onClickSendMsgByServer(View v){
        final Message msg = Message.obtain();
        msg.setData(new BundleHelper().putString("msg", etMessage.getText().toString())
                .getBundle());
        msg.what = WHAT_MSG;
        mServer.sendMessage(msg, mRandom.nextInt() % 2 +1);
    }

    private static final int WHAT_MSG = 1;
}
