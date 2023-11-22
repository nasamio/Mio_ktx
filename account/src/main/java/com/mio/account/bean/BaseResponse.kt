package com.mio.account.bean

import android.util.Log
import com.mio.account.net.Code
import com.mio.account.net.ErrorHelper
import com.mio.base.Tag.TAG

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T
) {
    fun getHandledData(): T? {
        Log.d(TAG, "getHandledData: $code,data:$data,msg:$message")
        if (code != Code.SUCCESS) {
            ErrorHelper.handleError(code)
        }
        return if (code == Code.SUCCESS) data else null
    }
}