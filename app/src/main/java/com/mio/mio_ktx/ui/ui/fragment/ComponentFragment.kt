package com.mio.mio_ktx.ui.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import com.mio.base.BaseFragment
import com.mio.base.extension.bg
import com.mio.base.extension.enablePressEffect
import com.mio.base.extension.load
import com.mio.base.extension.loadBlur
import com.mio.base.extension.loadCircle
import com.mio.base.extension.loadRound
import com.mio.base.setClickListener
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentComponentBinding


class ComponentFragment : BaseFragment<FragmentComponentBinding>(R.layout.fragment_component) {
    override fun initView() {
        testBgDrawable()
    }

    private fun testBgDrawable() {
        val url = "https://t7.baidu.com/it/u=3601447414,1764260638&fm=193&f=GIF"
        mDataBinding.ivTest.load(url)


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