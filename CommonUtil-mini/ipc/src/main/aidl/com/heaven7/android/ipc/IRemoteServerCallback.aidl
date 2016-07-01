package com.heaven7.android.ipc;

import android.os.Message;

interface IRemoteServerCallback {
       /**
         * processor the source message to a new message.
         * and the new message will reply to the client
         */
         Message processMessage(in int flag ,in Message msg);
}
