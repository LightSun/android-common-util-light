package com.heaven7.android.ipc;

import android.os.Message;

interface IRemoteServerCallback {
       /**
         * processor the source message to a new message.
         * and the new message will reply to the clients. this runs on a sub thread.
         * @param messagePolicy the policy of handle message
         * @param msg the source message
         * @return the new message , or null if the message is ignored.
         */
         Message processMessage(in int messagePolicy ,in Message msg);
}
