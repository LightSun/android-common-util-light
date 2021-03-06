package com.heaven7.android.mini.demo.sample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.heaven7.android.ipc.IpcConstant;
import com.heaven7.android.ipc.MessageClient;
import com.heaven7.android.ipc.MessageServer;
import com.heaven7.android.mini.demo.BaseActivity;
import com.heaven7.android.mini.demo.R;
import com.heaven7.core.util.BundleHelper;
import com.heaven7.core.util.Logger;

import java.util.List;
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
        getExplicitIntent(this, new Intent(IpcConstant.ACTION_MESSAGE_SERVICE));
        mClient = new MessageClient(this){
            @Override
            protected void afterConnected() {
                showToast("client is connected.");
                Logger.i(TAG, "MessageClient_afterConnected", "client is connected.");
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
    }
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
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
