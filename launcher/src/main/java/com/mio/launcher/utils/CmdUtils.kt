package com.mio.launcher.utils

import com.mio.launcher.bean.CommandResult
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader


object CmdUtils {
    private val LINE_SEP = System.getProperty("line.separator")

    fun execCmd(
        commands: Array<String>?,
        isRoot: Boolean,
        isNeedResultMsg: Boolean,
    ): CommandResult? {
        var result = -1
        if (commands == null || commands.size == 0) {
            return CommandResult(result, null, null)
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: StringBuffer? = null
        var errorMsg: StringBuffer? = null
        var os: DataOutputStream? = null
        try {
            //root过的手机上面获得root权限
            process = Runtime.getRuntime().exec(if (isRoot) "su" else "sh")
            os = DataOutputStream(process.outputStream)
            for (command in commands) {
                if (command == null) continue
                os.write(command.toByteArray())
                os.writeBytes(LINE_SEP)
                os.flush()
            }
            os.writeBytes("exit$LINE_SEP")
            os.flush()
            result = process.waitFor()
            if (isNeedResultMsg) {
                successMsg = StringBuffer()
                errorMsg = StringBuffer()
                successResult = BufferedReader(
                    InputStreamReader(
                        process.inputStream,
                        "UTF-8"
                    )
                )
                errorResult = BufferedReader(
                    InputStreamReader(
                        process.errorStream,
                        "UTF-8"
                    )
                )
                var line: String?
                if (successResult.readLine().also { line = it } != null) {
                    successMsg.append(line)
                    while (successResult.readLine().also { line = it } != null) {
                        successMsg.append(LINE_SEP).append(line)
                    }
                }
                if (errorResult.readLine().also { line = it } != null) {
                    errorMsg.append(line)
                    while (errorResult.readLine().also { line = it } != null) {
                        errorMsg.append(LINE_SEP).append(line)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
                errorResult?.close()
                process?.destroy()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return CommandResult(
            result, successMsg?.toString(),
            errorMsg?.toString()
        )
    }

    fun isRoot(): Boolean {
        val su = "su"
        //手机本来已经有root权限（/system/bin/su已经存在，adb shell里面执行su就可以切换root权限下）
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }
}