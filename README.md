# android-common-util-light
a light lib of android-common-utils for use.

##  ipc框架介绍及使用

  - 简述：ipc就是进程间通信, 所以本库就是进程间通信的框架。
          为了app间通信，我特意写了2个库：ipc和ipc-server (就是指客户端和服务端)

  - 原理： 稍后以图的形势补上. 
    
  - 已经使用ipc框架的案例：
        - 开源库 [android-fully-log](https://github.com/LightSun/android-fully-log)
        - android完整的日志系统，通过Ipc来读写日志。支持日志的格式化，加解密，过滤等。

  - 本库的特点：
      - 1, 支持app间的进程间通信。
      - 2, 支持多个客户端(client)和一个服务端(server)通信。服务端接收到消息后可以群发给其他客户端。
          ps: 当前设计只支持1个服务端.
      - 3, client 和 server 都可以发送消息。但是server端不能发送reply消息。        
      - 4，支持3种消息模式：
          - 广播(broadcast): 就是说server端接收到广播后可以 处理后直接下发给岁 所有的client。
          - 回执(reply)：    相当于1个client和1个server之间的通信。 server端接收到消息处理后回执消息给对应的client. 
          - 消费(comsume):   就是client或者server可以发送一条comsume的消息，只要有一个客户端消耗了，消息的传递也就终止了。
      - 5, 消息的处理在单独的子线程。(考虑到比如文件操作什么的)
      
    - 使用介绍：
       
       - 步骤1：添加权限 
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
        <uses-permission android:name="com.heaven7.android.ipc.service"/>
              导入对应的库：
 ``` java
   //客户端需要导入的
  compile 'com.heaven7.android.ipc:ipc:2.1.1'
  //服务端需要导入的
  compile 'com.heaven7.android.ipc.server:ipc-server:1.1.1'
 ```
      - 2：创建MessageClient和MessageServer对象。（代表消息客户端和服务端）
      - 3：绑定和解绑消息服务： 调用MessageClient.bind()/MessageClient.unbind() , MessageServer同理.
      - 4: 发送和接收消息。
          - 发送： boolean sendMessage(Message msg , @IpcConstant.MessagePolicy int policy)
                  -  (MessageClient和MessageServer这点基本相同，只是server端不能发送reply消息)
          - 处理/接收消息：
              - server只有1个方法：
                      //处理来源消息，返回新的消息，如果返回null就表示忽略这条消息
                      protected abstract Message processMessage(int policy, Message msg);
                        
              - client有3个方法分别代表3种消息。
 ```java
   /**  接收广播消息
     *  called when client receive a broadcast message from server
     * @param msg the new message
     */
    protected void onReceive(Message msg){

    }
    /** 处理消耗模式的消息
     * @param msg the new message
     * @return true if you handle it .
     */
    protected boolean consumeMessage(Message msg){
        return false;
    }
    /** 处理reply消息
     * called when server reply message to the message requester.
     * @param msg the new message
     */
    protected void handleReplyMessage(Message msg){

    }
 ```
      
               

## introduce
     1, module -> android-ipc
       -(1), desc: this is a common ipc lib of android.
       -(2), features: 
            - first, support multi clients communicate with a server.
            - second, support client can send a message to server, so is server.
            - third,  support multi work policy(or mode): broadcast, consume, reply
            - forth, server support work in background thread.
          -(3), calling flow:
```java
           // 1, init client and server .offen in multi app.
           mClient = new MessageClient(this){
             @Override
             protected void afterConnected() {
                showToast("client is connected.");
                Logger.i(TAG, "MessageClient_afterConnected", "client is connected.");
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
           };
           //2, bind and unbind
           //for client
           mClient.bind();  
           mClient.unbind();
           //for server
           mServer.bind();  
           mServer.unbind();
           // send message , client and server both can do it.
           boolean sendMessage(Message msg , @MessageService.MessagePolicy int policy);
           mClient.sendMessage(...);
           mServer.sendMessage(...);
```
      the more to see in demo [IpcTestActivity](https://github.com/LightSun/android-common-util-light/blob/master/CommonUtil-mini/app/src/main/java/com/heaven7/android/mini/demo/sample/IpcTestActivity.java).
    

## Gradle config

common util
 ``` java
compile 'com.heaven7.core.util:util-v1:1.0.9'
 ```
 
 util-extra
 ``` java
compile 'com.heaven7.core.util:util-extra:1.0'
 ```
 
 the useful memory util 
 ``` java
 compile 'com.heaven7.core.util:memory:1.0'
 ```
 
 common ipc lib 
 in client:
 ``` java
  compile 'com.heaven7.android.ipc:ipc:2.1.1'
 ```
  in server:
 ``` java
  compile 'com.heaven7.android.ipc.server:ipc-server:1.1.1'
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
