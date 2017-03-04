#include "com_android_ld_jni_html_aidl_MyJniLib.h"
JNIEXPORT jstring JNICALL Java_com_android_ld_jni_1html_1aidl_MyJniLib_getString
        (JNIEnv *env, jobject jclass){//  jint i,jstring s  如果还有参数这样添加

    return (*env)->NewStringUTF(env,"从C来的数据");
}

