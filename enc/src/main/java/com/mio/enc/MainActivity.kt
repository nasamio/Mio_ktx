package com.mio.enc

import MyWebSocketClient
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mio.base.Tag.TAG
import com.mio.s57.EncHelper
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gdal.gdal.gdal
import org.gdal.ogr.ogr
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
                startMove()
                // start read
//                read()
//                testTransform()
//                lifecycleScope.launch(Dispatchers.IO) {
////                    read()
//                    test()
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
        EncHelper.apply {
            load(path)
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
            copyFileFromAssets(
                this@MainActivity,
                "gdal-data",
                "data/data/com.mio.enc",
                "gdal-data"
            )

        }
        Log.d(TAG, "startMove: end...")

        // read()
        lifecycleScope.launch(Dispatchers.IO) {
            // loadLib()
            delay(2000)
            // testGdal()
            testWebSocket()
        }
    }

    /**
     * 一个web socket通信：http://localhost:8888/chat
     */
    private fun testWebSocket() {
        MyWebSocketClient().connectToWebSocket("ws://192.168.2.77:8888/chat",123)
    }

    private fun loadLib() {
        System.loadLibrary("gdalconstjni")
        System.loadLibrary("gdaljni")
        System.loadLibrary("ogrjni")
        System.loadLibrary("osrjni")
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
    
    

    private fun testGdal() {
        Log.d(TAG, "testGdal: start...")
        gdal.AllRegister()
        gdal.SetConfigOption("GDAL_DATA", "data/data/com.mio.enc/gdal-data")
//        gdal.set

        val option = gdal.GetConfigOption("GDAL_DATA")
        Log.d(TAG, "testGdal: $option")

        val ogrDriverCount = ogr.GetDriverCount()
        val gdalDriverCount = gdal.GetDriverCount()
        Log.d(TAG, "testGdal: ogr driver count:$ogrDriverCount, gdal driver count:$gdalDriverCount")
        for (i in 0 until ogr.GetDriverCount()) {
            val driverName = ogr.GetDriver(i).name
            Log.d(TAG, "testGdal: $driverName")
        }
        val driver = ogr.GetDriverByName("S57")
        val source = driver.Open("data/data/com.mio.enc/files/C1513359.000")
        source?.let {
            val description = it.GetDescription()




            val layerCount = it.GetLayerCount()
            Log.d(TAG, "testGdal: layer count:$layerCount")
            for (i in 0 until it.GetLayerCount()) {
                val layer = it.GetLayer(i)
                layer?.let {
                    val layerName = it.GetName()
                    val featureCount = it.GetFeatureCount()
                    Log.d(TAG, "testGdal: $layerName, feature count:$featureCount")
                    for (j in 0 until featureCount) {
                        val feature = it.GetFeature(j)
                        feature?.let {
                            val fieldCount = it.GetFieldCount()
                            val defnName = it.GetDefnRef().GetName()

                            val getGeometryName = it.GetGeometryRef()?.GetGeometryName()
                            Log.d(
                                TAG,
                                "testGdal: $defnName,${it.GetFID()},$getGeometryName, ${
                                    it.GetFieldAsString(0)
                                }"
                            )




                            for (k in 0 until fieldCount) {
                                //  Log.d(TAG, "testGdal: field type:$fieldType,value:${it.GetFieldAsString(k)}")
                                it.GetFieldDefnRef(k)?.let {
                                    val name = it.GetName()
                                    val type = it.GetFieldType()
                                    Log.d(TAG, "testGdal: field name:$name,type:$type")
                                }
                            }
                        }
                    }
                }
            }
        }


//        gdal.SetConfigOption("GDAL_DATA", "data/data/com.mio.enc/gdal-data")
//        Log.d(TAG, "testGadl: driver count1:${gdal.GetDriverCount()}")
//        val count = ogr.GetDriverCount()
//        Log.d(TAG, "testGadl: driver count:$count")
//        for (i in 0 until gdal.GetDriverCount()) {
//            val driverName = gdal.GetDriver(i).shortName
//            Log.d(TAG, "testGadl: $driverName")
//        }
//
//        val driver = gdal.GetDriverByName("S57")
//        Log.d(TAG, "testGadl: driver:$driver")
//
//        val ogrCount  = ogr.GetDriverCount()
//        Log.d(TAG, "testGadl: ogr count:$ogrCount")
//
////        val dataSet = driver.Open("data/data/com.mio.enc/files/C1513359.000", 0)
////
////        val layerCount = dataSet.GetLayerCount()
////        Log.d(TAG, "testGadl: layer count:$layerCount")
//
//        val openSwig0 = ogrJNI.Open__SWIG_0("data/data/com.mio.enc/files/C1513359.000", 0)
//        Log.d(TAG, "testGadl: open swing:$openSwig0")
//
//        val open = ogr.Open("data/data/com.mio.enc/files/C1513359.000", 0)
//        Log.d(TAG, "testGadl: file:$open")
//
//
//        gdal.GDALDestroyDriverManager()
    }

    class TestGadl {

    }
}
