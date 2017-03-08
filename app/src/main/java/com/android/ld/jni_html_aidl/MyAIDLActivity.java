package com.android.ld.jni_html_aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyAIDLActivity extends AppCompatActivity {

    private ServiceConnection mConn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            try {
                iBookManager.addBook(new Book("android开发艺术探索",1));
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
        setContentView(R.layout.activity_my_aidl);
        bindService(new Intent(this,MyService.class),mConn,BIND_AUTO_CREATE);
    }
}
