package com.android.ld.jni_html_aidl.messager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.ld.jni_html_aidl.R;

public class MessagerActivity extends AppCompatActivity {

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger(service);
            Message message = Message.obtain();
            message.what=1;
            Bundle bundle = new Bundle();
            bundle.putString("key","hello!");
            message.setData(bundle);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        bindService(new Intent(this, MessagerService.class), mConn, BIND_AUTO_CREATE);
    }
}
