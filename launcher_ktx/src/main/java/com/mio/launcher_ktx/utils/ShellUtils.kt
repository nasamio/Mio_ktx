package com.mio.launcher_ktx.utils

object ShellUtils {
    fun execCmd(cmd: String) {
        Runtime.getRuntime().exec(cmd)
    }
}