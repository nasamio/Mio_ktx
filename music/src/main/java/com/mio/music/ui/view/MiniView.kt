package com.mio.music.ui.view

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.lxj.xpopup.core.BottomPopupView
import com.mio.base.Tag.TAG
import com.mio.base.addChangeCallback
import com.mio.music.R
import com.mio.music.databinding.LayoutMiniBinding
import com.mio.music.manager.MusicPlayer

class MiniView(context: Context) : BottomPopupView(context) {
    private lateinit var mDataBinding: LayoutMiniBinding

    override fun addInnerContent() {
        val contentView =
            LayoutInflater.from(context).inflate(implLayoutId, bottomPopupContainer, false)
        bottomPopupContainer.addView(contentView)
        mDataBinding = DataBindingUtil.bind(contentView)!!
        initView()
        initData()
        popupInfo.apply {
            // Status bar
            animationDuration = 3000
            enableDrag = false
            hasShadowBg = false
            isTouchThrough = true
            isClickThrough = true
            isDismissOnTouchOutside = false
        }
    }

    private fun initView() {
        // Add any additional view initialization logic here
    }

    private fun initData() {
        MusicPlayer.progress.addChangeCallback {
//            mDataBinding.progressIndicator.progress = (it * 100f).toInt()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_mini
    }

    override fun processKeyEvent(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "processKeyEvent: $keyCode")
        return false
    }
}
