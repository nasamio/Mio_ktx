package com.mio.filemanager.helper

import android.util.Log
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelShell
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import com.mio.base.Tag.TAG
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintWriter
import java.nio.charset.Charset
import java.util.Properties


object SftpHelper {
    val usename = "ubuntu"
    val password = "Qq123456"
    val host = "117.50.190.141"
    val port = 22
    var isConnect = false
    lateinit var session: Session
    lateinit var sftpChannel: com.jcraft.jsch.ChannelSftp
    lateinit var channelShell: ChannelShell
    lateinit var channelShellOutPutStream: OutputStream
    lateinit var channelExec: ChannelExec
    lateinit var printWriter: PrintWriter
    val ansiEscape = "\u001B\\[[;\\d]*m".toRegex()
    fun connect(
        connectRes: (isConnect: Boolean) -> Unit,
        execRes: (res: String) -> Unit,
    ) {
        val jsch = JSch()
        session = jsch.getSession(usename, host, port)
        session.setPassword(password)
        val config = Properties()
        config.put("StrictHostKeyChecking", "no")
        session.setConfig(config)
        session.timeout = 3000
        session.connect()
        val result = session.isConnected
        Log.d(TAG, "connect: session.isConnected: $result")
        isConnect = result
        if (isConnect) {
            sftpChannel = session.openChannel("sftp") as com.jcraft.jsch.ChannelSftp
            sftpChannel.connect()
            channelShell = session.openChannel("shell") as ChannelShell
            channelShell.connect()

            val inputStream = channelShell.inputStream
            channelShellOutPutStream = channelShell.outputStream
            printWriter = PrintWriter(channelShellOutPutStream)

            val tmp = ByteArray(1024)

            channelExec = session.openChannel("exec") as ChannelExec


            while (true) {
                while (inputStream.available() > 0) {
                    val i = inputStream.read(tmp, 0, 1024)
                    if (i < 0) break
                    val cleanString =
                        String(tmp, 0, i, Charset.forName("UTF-8"))
                            .replace(ansiEscape, "") // 去除颜色
                    Log.d(TAG, cleanString)
                    execRes(cleanString)
                }
                if (channelShell.isClosed) {
                    if (inputStream.available() > 0) continue
                    Log.d(TAG, "connect: exit-status: ${channelShell.exitStatus}")
                    break
                }
            }

//            execCommandByShell("ls")

//            ls()
//            exec2("ls")
//            exec2("cd file/")
//            exec2("ls")
            connectRes(result)

        }
    }

    fun cd(path: String) {
        sftpChannel.cd(path)
    }

    fun ls() {
        val ls = sftpChannel.ls("")
        Log.d(TAG, "ls: $ls")
    }

    // 往channelShellOutPutStream中写数据
    fun exec2(command: String) {
        Log.d(TAG, "exec2: command: $command")
        channelShellOutPutStream.write(command.toByteArray())
        channelShellOutPutStream.flush()
    }

    fun exec(command: String) {
        Log.d(TAG, "----> $command")

        if (!isConnect) {
            Log.d(TAG, "Not connected to the server")
            return
        }


        channelExec.setCommand(command + "\n")
        channelExec.connect()

        val reader = BufferedReader(InputStreamReader(channelExec.inputStream))
        var line: String?
        val output = StringBuilder()

        while (reader.readLine().also { line = it } != null) {
            output.append("$line  ")
        }
        if (output.isNotEmpty()) {
            Log.d(TAG, "----> $output")
        } else {

        }

//        channelExec.disconnect()
    }

    @Throws(IOException::class, JSchException::class)
    fun execCommandByShell(cmd: String): String {
        Log.d(TAG, "execCommandByShell: cmd: $cmd")
        if (channelShell.isClosed) {
            if (!session.isConnected) {
                session.connect()
            }
            channelShell.connect()
        }

        printWriter.println(cmd)
        printWriter.flush()
//        val result = ""
//
//        //2.尝试解决 远程ssh只能执行一句命令的情况
//        val channelShell = session.openChannel("shell") as ChannelShell
//        val inputStream = channelShell.inputStream //从远端到达的数据  都能从这个流读取到
//        channelShell.setPty(true)
//        channelShell.connect()
//        val outputStream = channelShell.outputStream //写入该流的数据  都将发送到远程端
//        //使用PrintWriter 就是为了使用println 这个方法
//        //好处就是不需要每次手动给字符加\n
//        val printWriter = PrintWriter(outputStream)
//        printWriter.println("ls")
//        printWriter.println("cd file/")
//        printWriter.println("ls")
//        printWriter.println("exit") //为了结束本次交互
//        printWriter.flush() //把缓冲区的数据强行输出
//        /**
//         * shell管道本身就是交互模式的。要想停止，有两种方式：
//         * 一、人为的发送一个exit命令，告诉程序本次交互结束
//         * 二、使用字节流中的available方法，来获取数据的总大小，然后循环去读。
//         * 为了避免阻塞
//         */
//        val tmp = ByteArray(1024)
//        while (true) {
//            while (inputStream.available() > 0) {
//                val i = inputStream.read(tmp, 0, 1024)
//                if (i < 0) break
//                val s = String(tmp, 0, i)
//                if (s.indexOf("--More--") >= 0) {
//                    outputStream.write(" ".toByteArray())
//                    outputStream.flush()
//                }
////                println(s)
//                Log.d(TAG, "execCommandByShell: s: $s")
//            }
//            if (channelShell.isClosed) {
////                println("exit-status:" + channelShell.exitStatus)
//                Log.d(TAG, "execCommandByShell: exit-status: ${channelShell.exitStatus}")
//                break
//            }
//            try {
//                Thread.sleep(1000)
//            } catch (e: Exception) {
//            }
//        }
//        outputStream.close()
//        inputStream.close()
//        channelShell.disconnect()
//        session.disconnect()
////        println("DONE")
//        Log.d(TAG, "execCommandByShell: result: $result")
        return "result"
    }
}