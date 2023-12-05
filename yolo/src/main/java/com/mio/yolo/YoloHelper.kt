package com.mio.yolo

import android.content.res.AssetManager
import android.graphics.Bitmap

class YoloHelper {
    public external fun Init(mgr: AssetManager)

    class Obj(
        var x: Float,
        var y: Float,
        var w: Float,
        var h: Float,
        var label: Int,
        var prob: Float,
    )

    public external fun Detect(bitmap: Bitmap, useGpt: Boolean): List<Obj>

    companion

    object {
        init {
            System.loadLibrary("yolov5ncnn")
        }
    }
}