package com.mio.music.data

data class LoginBean(val code: Int = 0,
                     val cookie: String = "",
                     val loginType: Int = 0,
                     val profile: Profile,
                     val bindings: List<BindingsItem>?,
                     val account: Account,
                     val token: String = "")