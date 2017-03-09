package com.android.ld.jni_html_aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.ld.jni_html_aidl.aidl.Book;
import com.android.ld.jni_html_aidl.aidl.IBookAddListener;
import com.android.ld.jni_html_aidl.aidl.IBookManager;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MyAIDLActivity extends AppCompatActivity {

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManager.Stub.asInterface(service);
            try {
                mIBookManager.addBook(new Book("android开发艺术探索", 3));
                mIBookManager.regAddListener(mAddListener);
                List<Book> bookList = mIBookManager.getBookList();
                Log.e("tag", bookList.toString());
                Log.e("tag", Thread.currentThread() + "11111");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //当服务被中断的时候回调
            Log.e("tag", "name =" + name);
            Log.e("tag", "已中断当前Binder");
            bindService(new Intent(MyAIDLActivity.this, MyService.class), mConn, BIND_AUTO_CREATE);

        }
    };
    IBookAddListener.Stub mAddListener = new IBookAddListener.Stub() {
        @Override
        public void OnBookAdd(Book newBook) throws RemoteException {
            //在客户端的Binder线程池中  为了操作UI所以要切换为主线程中
            mHandler.obtainMessage(1, newBook).sendToTarget();

            //[Binder:32233_1,5,main]2222
            Log.e("tag", Thread.currentThread() + "2222");
        }
    };
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e("tag", "图书馆新加了" + msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private IBookManager mIBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aidl);
        bindService(new Intent(this, MyService.class), mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        //在解除注册的时候注意  因为aidl的跨进程时会把mAddListener（对象在跨进程的时候反序列化）
        // 转成一个新的对象到服务端  两边不在同一个虚拟机 内存地址不一样自然无法解除
        if (mAddListener != null) {
            try {
                mIBookManager.unRegAddListener(mAddListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConn);
        super.onDestroy();
    }

    //如果服务端的代码是耗时操作就放在子线程执行
    public void getBooks(View view) {
        Log.e("tag", "getBook");
        Observable.just(1)
                .map(new Func1<Integer, List<Book>>() {
                    @Override
                    public List<Book> call(Integer integer) {
                        try {
                            return mIBookManager.getBookList();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Book>>() {
                    @Override
                    public void call(List<Book> books) {
                        if (books != null)
                            Log.e("tag", "getBook" + books.toString());
                        else
                            Log.e("tag", "getBook = null");
                    }
                });
    }
}
