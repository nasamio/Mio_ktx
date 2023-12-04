package com.mio.account.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Helper {
    fun now(): Long {
        return System.currentTimeMillis()
    }

}