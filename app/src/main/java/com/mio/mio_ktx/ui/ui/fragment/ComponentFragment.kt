package com.mio.mio_ktx.ui.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.extension.bg
import com.mio.base.extension.enablePressEffect
import com.mio.base.extension.load
import com.mio.base.extension.loadBlur
import com.mio.base.extension.loadCircle
import com.mio.base.extension.loadRound
import com.mio.base.setClickListener
import com.mio.base.utils.KvHelper
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentComponentBinding


class ComponentFragment : BaseFragment<FragmentComponentBinding>(R.layout.fragment_component) {
    override fun initView() {
        testBgDrawable()
        testKv()
    }

    private fun testKv() {
        Log.d(TAG, "testKv: ")
        // 初始化
        KvHelper.init(mContext)

        // 读取键值
        val v1 = KvHelper.getString("key1", "default")
        val v2 = KvHelper.getInt("key2", 0)
        val v3 = KvHelper.getBoolean("key3", false)
        val v4 = KvHelper.getFloat("key4", 0f)
        val v5 = KvHelper.getLong("key5", 0)
        // 打印: v1:value1, v2:1, v3:true, v4:1.0, v5:1
        Log.d("mio_tag", "v1:$v1, v2:$v2, v3:$v3, v4:$v4, v5:$v5")

        // 设置键值
        KvHelper.saveString("key1", "value1")
        KvHelper.saveInt("key2", 1)
        KvHelper.saveBoolean("key3", true)
        KvHelper.saveFloat("key4", 1.0f)
        KvHelper.saveLong("key5", 1)

        // 存储自定义数据类型
        data class User(val name: String, val age: Int)

        val user2 = KvHelper.getObject("user", User::class.java)
        Log.d(TAG, "testKv: user:$user2")
        
        val user = User("mio", 18)
        KvHelper.saveObject("user", user)

    }

    private fun testBgDrawable() {
        val url = "https://t7.baidu.com/it/u=3601447414,1764260638&fm=193&f=GIF"
        mDataBinding.ivTest.apply {
            // 加载图片
            load(url)
            load(R.drawable.ic_app)
            // 圆角
            loadRound(url, 20f)
            // 圆形
            loadCircle(url)
            // 模糊
            loadBlur(url, 20)
        }


//        mDataBinding.tv1.apply {
//            // 背景圆角
//            bg(mContext.getColor(com.mio.base.R.color.black_30), 5f, 5)
//            // 前景按压
//            enablePressEffect()
//        }
//        mDataBinding.btn1.apply {
//            // 背景圆角
//            bg(mContext.getColor(com.mio.base.R.color.black_30), 20f, 5)
//
//            setClickListener {
//                val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
//                val intentUri = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
//                    .appendQueryParameter(
//                        "file",
//                        "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Avocado/glTF/Avocado.gltf"
//                    )
//                    .appendQueryParameter("mode", "ar_only")
//                    .build()
//                sceneViewerIntent.setData(intentUri)
//                sceneViewerIntent.setPackage("com.google.ar.core")
//                // startActivity(sceneViewerIntent)
//            }
//        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}