package com.android.ld.jni_html_aidl.aidl;

import android.os.RemoteException;

/**
 * Created by ${lida} on 2017/3/10.
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
