package com.android.ld.jni_html_aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MyAIDLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aidl);
        bindService(new Intent(this, MyService.class),connection ,BIND_AUTO_CREATE);
    }
    ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("tag", "onServiceDisconnected. tname:" + Thread.currentThread().getName());//主线程中
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            try {
                iBookManager.basicTypes(1,1l,true,1,1,"111");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("tag", "onServiceDisconnected. tname:" + Thread.currentThread().getName());//主线程中

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
