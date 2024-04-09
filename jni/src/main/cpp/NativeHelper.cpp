//
// Created by nasam on 2024/2/18.
//
#include <jni.h>
#include "com_mio_jni_NativeHelper.h"
#include <string>
#include <jni.h>
#include <jni.h>
#include <jni.h>

extern "C"
JNIEXPORT jint
JNICALL
        Java_com_mio_jni_NativeHelper_add(JNIEnv * env, jclass
clazz,
jint i, jint
i2) {
return i +
i2;
}

extern "C"
JNIEXPORT jstring
JNICALL
        Java_com_mio_jni_NativeHelper_stringFromJni(JNIEnv * env, jclass
clazz) {
std::string str = "Hello from JNI on NativeHelper";
return env->
NewStringUTF(str
.

c_str()

);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mio_jni_NativeHelper_nativeInit(JNIEnv
* env,
jclass clazz
) {
}