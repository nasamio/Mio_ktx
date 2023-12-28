package com.mio.music.data

data class FreeTimeTrialPrivilege(val userConsumable: Boolean = false,
                                  val resConsumable: Boolean = false,
                                  val remainTime: Int = 0,
                                  val type: Int = 0)