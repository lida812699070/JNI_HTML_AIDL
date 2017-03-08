package com.android.ld.jni_html_aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by ${lida} on 2017/3/4.
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IBookManager.Stub() {
            @Override
            public List<Book> getBookList() throws RemoteException {
                return null;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                Log.e("tag",book.toString());
            }
        };
    }
}
