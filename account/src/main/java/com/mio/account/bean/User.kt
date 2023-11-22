package com.mio.account.bean

import android.os.Build
import androidx.annotation.RequiresApi
import com.mio.base.extension.normalTime


data class User(
    var name: String,
    var password: String,
    var id: Int,
    var level: Int,
    var createTime: String,
    var vipTime: String
) {
    constructor(name: String, password: String) : this(
        name,
        password,
        0,
        0,
        "2023-11-22T05:33:54.000+00:00",
        "2023-11-22T05:33:54.000+00:00"
    )

    fun useDays(): Int {
        return ((vipTime.normalTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
    }

    fun isVip(): Boolean {
        return vipTime.normalTime() >= System.currentTimeMillis()
    }
}
