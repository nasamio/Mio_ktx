package com.mio.music.helper

import androidx.databinding.ObservableBoolean
import com.mio.music.data.Account
import com.mio.music.data.BindingsItem
import com.mio.music.data.Profile

object UserHelper {
    fun init() {
        token = SpHelper.getString("token")
        cookie = SpHelper.getString("cookie")
    }

    // 是否登录
    var loginType: Int = 0
    var account: Account? = null
    var token: String? = null
        set(value) {
            field = value
            SpHelper.saveString("token", value ?: "")
        }
    var profile: Profile? = null
    var bindings: List<BindingsItem>? = null
    var cookie: String? = null
        set(value) {
            field = value
            SpHelper.saveString("cookie", value ?: "")
        }

    fun isLogin(): Boolean = token != null && token!!.isNotEmpty()
}
