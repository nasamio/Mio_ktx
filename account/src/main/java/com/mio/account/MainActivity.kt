package com.mio.account

import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.mio.account.bean.User
import com.mio.account.fragment.MainFragment
import com.mio.account.fragment.MineFragment
import com.mio.account.fragment.MoreFragment
import com.mio.account.fragment.StatisticsFragment
import com.mio.account.net.KtorHelper
import com.mio.account.net.NetHelper
import com.mio.base.BaseFragmentActivity
import com.mio.base.Tag.TAG
import com.mio.base.extension.toNormalTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseFragmentActivity(
    mutableListOf(
        MainFragment(),
        MainFragment(),
        StatisticsFragment(),
        MoreFragment(),
        MineFragment(),
    ),
    R.menu.fragments
) {
    override fun initV() {
        // 隐藏标题栏
        supportActionBar?.hide()
        // 透明状态栏
        window.statusBarColor = getColor(com.mio.base.R.color.black_30)
        // 背景色
        mDataBinding.root.setBackgroundColor(getColor(R.color.bg_color))
        // tab背景色
        mDataBinding.bt.setBackgroundColor(getColor(R.color.tab_bg_color))

        lifecycleScope.launch {
            delay(1000)
            mDataBinding.bt.checkPos.set(2)
        }

        NetHelper.initApiService()
    }

    override fun initObserver() {
//        MaterialAlertDialogBuilder(this)
//            .setTitle("标题")
//            .setMessage("内容")
//            .setPositiveButton("确定") { dialog, which ->
//                Log.d(TAG, "onDraw: 确定")
//            }
//            .setNegativeButton("取消") { dialog, which ->
//                Log.d(TAG, "onDraw: 取消")
//            }
//            .show()
    }

    override fun initData() {
        lifecycleScope.launch {
//            val response = NetHelper.apiService.login(
//                User("zed", "zed2")
//            )
//            Log.d(TAG, "initData: $response")

            /*        val response = NetHelper.apiService.register(
                        User("mio", "123456")
                    )
                    Log.d(TAG, "initData: $response")*/

            testKtor()
        }
    }

    private fun testKtor() {
        lifecycleScope.launch {
            // 测试登录
            val response = KtorHelper.login(User("rz", "rz"))
            Log.d(TAG, "initData: $response")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Log.d(TAG, "testKtor: normalTime:${response.data.createTime?.toNormalTime()}")
//            }

            // 测试注册
//            val response = KtorHelper.register(User("rz3", "rz3"))
//            Log.d(TAG, "initData: $response")

            // 测试上传图片

//            AndPermission.with(this@MainActivity)
//                .runtime()
//                .permission(
//                    arrayOf(
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//                ).onGranted {
//                    Log.d(TAG, "testKtor: 权限已授予")
//
//                    val pickPhoto = Intent(
//                        Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                    )
//                    startActivityForResult(pickPhoto, 1001)
//
//
//                }.onDenied {
//                    Log.d(TAG, "testKtor: 权限已拒绝")
//                }.start()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001) {
            if (data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(
                    selectedImage!!,
                    filePathColumn, null, null, null
                )
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()

                Log.d(TAG, "onActivityResult: picturePath:$picturePath")

                lifecycleScope.launch {
                    val response = KtorHelper.upload(picturePath)
                    Log.d(TAG, "onActivityResult: $response")
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }
}