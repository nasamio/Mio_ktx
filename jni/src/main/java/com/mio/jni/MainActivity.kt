package com.mio.jni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: ${NativeHelper.stringFromJni()}")

        Log.d(TAG, "onCreate: 1+2=${NativeHelper.add(1, 2)}")

        Log.d(TAG, "onCreate: cores count:${ThreadAffinity.getCoresCount()}")

//        task1()
//        task2()
        for (i in 0..7) {
            task(i)
        }
    }

    private fun task(core: Int, dataSize: Long = 100_000_000L) {
        ThreadAffinity.pidToCore(android.os.Process.myPid(), 4)
        Thread {
            var time = System.currentTimeMillis()
            var sum = 0L
            for (i in 0..dataSize) {
                sum += i
            }
            time = System.currentTimeMillis() - time
            Log.e(TAG, "core($core)speed time: $time ms")
        }.start()
    }

    // 耗时任务1
    private fun task1() {
        Thread {
            var time = System.currentTimeMillis()
            var sum = 0L
            for (i in 0..1000000000L) {
                sum += i
            }
            time = System.currentTimeMillis() - time
            Log.e("SOC_", "start1: $time")
        }.start()
    }

    // 耗时任务2
    private fun task2() {
        Thread {
            var time = System.currentTimeMillis()
            var sum = 0L
            for (i in 0..1000000000L) {
                sum += i
            }
            time = System.currentTimeMillis() - time
            Log.e("SOC_", "start2: $time")
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}