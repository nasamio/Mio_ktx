package com.mio.enc

import BytesHelper
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mio.base.Tag.TAG
import com.mio.enc.helper.S57Helper
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 权限
        requestPermission()

        setContentView(R.layout.activity_main)
    }

    private fun requestPermission() {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .onGranted {
                // start move
//                startMove()
                // start read
//                read()
                testTransform()
//                lifecycleScope.launch(Dispatchers.IO) {
////                    read()
//                    test()
//                    read()
//                }
            }
            .start()
    }

    private fun testTransform() {
        val input = arrayOf(
            0x8b, 0xed, 0x97, 0xf3, 0x59, 0x29, 0x4e, 0x0b
        )
        val bytes: ByteArray = ByteArray(input.size)
        repeat(input.size) {
            bytes[it] = input[it].toByte()
        }

        val output = String(bytes, Charset.forName("UTF-32"))
        Log.d(TAG, "testTransform: $output")
    }

    private fun test() {
        var size = 1558
        val jz = 128
        var total = 8 * jz + 5 * jz * jz + 5 * jz * jz * jz + 1 * jz * jz * jz * jz
        Log.d(TAG, "test total: $total")
    }

    private fun read3() {
        val path = "data/data/${packageName}/files/C1513359.000"
//        val mod = S57ModuleReader()
    }

    private suspend fun read2() {
        val path = "data/data/${packageName}/files/C1513359.000"
//        val s57 = S57().apply {
//            load(path)
//        }
    }

    private fun read() {
        val path = "data/data/${packageName}/files/C1513359.000"
        S57Helper.init(path)
        S57Helper.data.forEachIndexed { index, lr ->
            Log.d(TAG, "read2:$index \n $lr")
        }

    }

    /**
     * 把assets文件夹下的文件移动到 data/data/包名下
     */
    private fun startMove() {
        Log.d(TAG, "startMove: start...")
        lifecycleScope.launch(Dispatchers.IO) {
            copyFileFromAssets(
                this@MainActivity,
                "files/C1513359.000",
                "data/data/com.mio.enc/files",
                "C1513359.000"
            )
        }
        Log.d(TAG, "startMove: end...")
    }

    /**
     * 将 assets 文件拷贝到指定路径
     *
     * @param context 上下文
     * @param assetName 要拷贝的 asset 文件名
     * @param savePath 目标路径
     * @param saveName 目标文件名
     */
    private suspend fun copyFileFromAssets(
        context: Context,
        assetName: String,
        savePath: String,
        saveName: String,
    ) {
        // 检查目标文件夹是否存在，如果不存在则创建
        val dir = File(savePath)
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.d(TAG, "创建目录失败: $savePath")
                return
            }
        }

        // 拷贝文件
        val filename = "$savePath/$saveName"
        val file = File(filename)
        if (!file.exists()) {
            try {
                val inputStream = context.assets.open(assetName)
                val fileOutputStream = FileOutputStream(filename)

                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead)
                }

                fileOutputStream.flush()
                fileOutputStream.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}