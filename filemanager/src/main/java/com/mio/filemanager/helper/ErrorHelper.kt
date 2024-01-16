package com.mio.filemanager.helper

import android.util.Log
import com.mio.base.Tag.TAG

object ErrorHelper {
    fun post(throwable: Throwable) {
        Log.e(TAG, "post: $throwable")
    }
}