package com.mio.music.helper

import androidx.databinding.ObservableBoolean
import com.mio.music.data.Account
import com.mio.music.data.BindingsItem
import com.mio.music.data.Profile

object UserHelper {
    fun init() {
        token = SpHelper.getString("token")
        cookie = SpHelper.getString("cookie")
        uid = SpHelper.getString("uid").toLongOrNull()
        nickname = SpHelper.getString("nickname")
        avatarUrl = SpHelper.getString("avatarUrl")
        backgroundUrl = SpHelper.getString("backgroundUrl")
    }

    // 是否登录
    var loginType: Int = 0
    var account: Account? = null
    var token: String? = null
        set(value) {
            field = value
            SpHelper.saveString("token", value ?: "")
        }
    var uid: Long? = null
        set(value) {
            field = value
            SpHelper.saveString("uid", value?.toString() ?: "")
        }
    var profile: Profile? = null
    var bindings: List<BindingsItem>? = null
    var cookie: String? = null
        set(value) {
            field = value
            SpHelper.saveString("cookie", value ?: "")
        }
    var nickname: String? = null
        set(value) {
            field = value
            SpHelper.saveString("nickname", value ?: "")
        }
    var avatarUrl: String? = null
        set(value) {
            field = value
            SpHelper.saveString("avatarUrl", value ?: "")
        }
    var backgroundUrl: String? = null
        set(value) {
            field = value
            SpHelper.saveString("backgroundUrl", value ?: "")
        }

    fun isLogin(): Boolean = /*token != null && token!!.isNotEmpty()*/
        uid != null && uid!! > 0
}
