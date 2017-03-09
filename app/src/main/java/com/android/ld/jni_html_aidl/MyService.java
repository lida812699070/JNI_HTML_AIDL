package com.android.ld.jni_html_aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.ld.jni_html_aidl.aidl.Book;
import com.android.ld.jni_html_aidl.aidl.IBookAddListener;
import com.android.ld.jni_html_aidl.aidl.IBookManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ${lida} on 2017/3/4.
 */
public class MyService extends Service {
    //防止并发  所以用这个集合代替ArrayList 在返回binder的过程中会自己按照Arraylist的规则转化 所以客户端收到的List还是ArrayList
    CopyOnWriteArrayList<Book> mList = new CopyOnWriteArrayList<>();
    //在解除注册的时候注意  因为aidl的跨进程时会把mAddListener（对象在跨进程的时候反序列化）
    // 转成一个新的对象到服务端  两边不在同一个虚拟机 内存地址不一样自然无法解除
    //RemoteCallbackList专门用于解决这个问题  内部是一个Map集合 key=listener.asBinder  Callback value=new Callback(listener, cookie);
    RemoteCallbackList<IBookAddListener> mListenerList = new RemoteCallbackList<IBookAddListener>();
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new IBookManager.Stub() {
            @Override
            public List<Book> getBookList() throws RemoteException {
                //模拟耗时操作
                SystemClock.sleep(1000);
                return mList;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                mList.add(book);
            }

            @Override
            public void regAddListener(IBookAddListener listener) throws RemoteException {
                mListenerList.register(listener);

                Log.e("tag", "add success" + mListenerList.getRegisteredCallbackCount());
                //这个集合无法解除注册
               /* if (!mListenerList.contains(listener)) {
                    mListenerList.add(listener);
                    Log.e("tag", "add success");
                } else {
                    Log.e("tag", "is exit");
                }
                Log.e("tag", "size=" + mListenerList.size());*/
            }

            @Override
            public void unRegAddListener(IBookAddListener listener) throws RemoteException {
                mListenerList.unregister(listener);
                Log.e("tag", "remove success" + mListenerList.getRegisteredCallbackCount());
                /*if (!mListenerList.contains(listener)) {
                    Log.e("tag", "is no exit");
                } else {
                    mListenerList.remove(listener);
                    Log.e("tag", "remove success");
                }
                Log.e("tag", "size=" + mListenerList.size());*/
            }

            @Override
            public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                // 权限验证
                int check = checkCallingOrSelfPermission("com.xsj.siji.permission.ACCESS_BOOK_SERVICE");
                Log.e("tag", "check:" + check);
                if (check == PackageManager.PERMISSION_DENIED) {
                    Log.e("tag", "Binder 权限验证失败");
                    return false;
                }
                // 包名验证
                String packageName = null;
                String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
                if (packages != null && packages.length > 0) {
                    packageName = packages[0];
                }
                if (!packageName.startsWith("com.android")) {
                    Log.e("tag", "包名验证失败");
                    return false;

                }
                return super.onTransact(code, data, reply, flags);
            }
        };
    }

    int i = 0;

    @Override
    public void onCreate() {
        mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Book newBook = new Book("第" + i + "本书", i);
                try {
                    onNewBookAdd(newBook);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        }, 2000, 5000);
        super.onCreate();
    }

    private void onNewBookAdd(Book newBook) {
        mList.add(newBook);
        int callNumber = mListenerList.beginBroadcast();
        for (int i = 0; i < callNumber; i++) {
            IBookAddListener l = mListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.OnBookAdd(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }


    @Override
    public void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDestroy();
    }
}
