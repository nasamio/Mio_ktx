package com.mio.music.helper

import androidx.databinding.ObservableBoolean
import com.mio.music.data.Account
import com.mio.music.data.BindingsItem
import com.mio.music.data.Profile

object UserHelper {
    // 是否登录
    var isLogin = ObservableBoolean(false)
    var loginType: Int = 0
    var account: Account? = null
    var token: String? = null
    var profile: Profile? = null
    var bindings: List<BindingsItem>? = null
    var cookie: String? = null

}
