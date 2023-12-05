package com.mio.yolo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class YoloHelper2 {
    public native Boolean Init(AssetManager assetManager);

    public class Obj {
        public float x;
        public float y;
        public float w;
        public float h;
        public String label;
        public float prob;
    }

    public native Obj[] Detect(Bitmap bitmap, boolean use_gpu);

    static {
        System.loadLibrary("yolov5ncnn");
    }
}
