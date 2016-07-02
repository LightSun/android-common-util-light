# android-common-util-light
a light lib of android-common-utils for use.

## introduce
   - 1, module -> android-ipc
     -(1), desc: this is a common ipc lib of android.
     -(2), features: 
            - first, support multi clients communicate with a server.
            - second, support client can send a message to server, so is server.
            - third,  support multi work policy(or mode): broadcast, consume, reply
            - forth, server support work in background thread.
     -(3), calling flow:
      ``` java
         // 1, init client and server .offen in multi app.
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
            
            //broadcast policy message
            @Override
            protected void onReceive(Message msg) {
                Logger.i(TAG, "MessageClient_onReceive", toTestString(msg));
            }
            //consume policy message
            @Override
            protected boolean consumeMessage(Message msg) {
                Logger.i(TAG, "MessageClient_consumeMessage", toTestString(msg));
                return super.consumeMessage(msg);
            }
            // reply policy message.
            @Override
            protected void handleReplyMessage(Message msg) {
                Logger.i(TAG, "MessageClient_handleReplyMessage", toTestString(msg));
            }
        };
        mServer = new MessageServer(this){
            
            //this is very important for server handle client message.
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
        //2, bind and unbind
           //for client
           mClient.bind();  
           mClient.unbind();
           //for server
           mServer.bind();  
           mServer.unbind();
        // send message , client and server both can do it.
        void sendMessage(Message msg , @MessageService.MessagePolicy int policy);
         mClient.sendMessage(...);
         mServer.sendMessage(...);
       ```
    

## Gradle config

common util
 ``` java
compile 'com.heaven7.core.util:util-v1:1.0.7'
 ```
 
 util-extra
 ``` java
compile 'com.heaven7.core.util:util-extra:1.0'
 ```
 
 the useful memory util 
 ``` java
 compile 'com.heaven7.core.util:memory:1.0'
 ```
 
 common ipc lib (beta)
 ``` java
  compile 'com.heaven7.android.ipc:ipc:1.0'
 ```

## hope
i like technology. especially the open-source technology.And previous i didn't contribute to it caused by i am a little lazy, but now i really want to do some for the open-source. So i hope to share and communicate with the all of you.


## License

    Copyright 2015   
                    heaven7(donshine723@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
