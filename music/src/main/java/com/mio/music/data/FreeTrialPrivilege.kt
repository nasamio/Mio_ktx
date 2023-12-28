package com.mio.music.data

data class FreeTrialPrivilege(val userConsumable: Boolean = false,
                              val resConsumable: Boolean = false,
                              val cannotListenReason: Any? = null,
                              val playReason: Any? = null,
                              val listenType: Any? = null)