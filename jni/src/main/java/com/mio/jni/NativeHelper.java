package com.mio.jni;

public class NativeHelper {
    static {
        System.loadLibrary("mio_jni");
    }

    public static native void nativeInit();

    public static native int add(int i, int i2);

    public static native String stringFromJni();
}
