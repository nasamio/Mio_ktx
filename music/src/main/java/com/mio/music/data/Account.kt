package com.mio.music.data

data class Account(val salt: String? = "",
                   val vipType: Int? = 0,
                   val userName: String = "",
                   val type: Int = 0,
                   val ban: Int = 0,
                   val anonimousUser: Boolean = false,
                   val createTime: Long = 0,
                   val tokenVersion: Int = 0,
                   val id: Int = 0,
                   val whitelistAuthority: Int = 0,
                   val baoyueVersion: Int = 0,
                   val viptypeVersion: Long = 0,
                   val donateVersion: Int = 0,
                   val status: Int = 0,
                   val uninitialized: Boolean = false)