package com.mio.account.bean

import com.mio.account.utils.Helper
import kotlinx.serialization.Serializable


@Serializable
data class User(
    var id: Int,
    var name: String,
    var password: String,
    var level: Int?,
    var createTime: Long?,
    var vipTime: Long?,
) {
    constructor(
        name: String,
        password: String,
    ) : this(
        0,
        name,
        password,
        0,
        Helper.now(),
        Helper.now()
    )
}

