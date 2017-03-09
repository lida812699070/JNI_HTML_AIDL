package com.android.ld.jni_html_aidl.messager;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ${lida} on 2017/3/8.
 */
public class MessagerService extends Service {

    private static class MessagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String key = msg.getData().getString("key");
                    Log.e("tag", "收到消息" + key);
                    Messenger messenger = msg.replyTo;
                    if (messenger != null) {
                        Message message = Message.obtain();
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("back", "服务端我收到了，待会回复你");
                        message.setData(bundle);
                        try {
                            messenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Messenger mMessenger = new Messenger(new MessagerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
