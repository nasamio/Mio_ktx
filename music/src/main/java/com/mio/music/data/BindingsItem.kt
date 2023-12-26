package com.mio.music.data

data class BindingsItem(val expiresIn: Int = 0,
                        val expired: Boolean = false,
                        val tokenJsonStr: String = "",
                        val refreshTime: Int = 0,
                        val id: Long = 0,
                        val type: Int = 0,
                        val bindingTime: Long = 0,
                        val userId: Int = 0,
                        val url: String = "")