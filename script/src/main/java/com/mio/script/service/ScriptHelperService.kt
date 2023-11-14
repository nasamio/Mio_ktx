package com.mio.script.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.mio.base.Tag.TAG

/**
 * 参考杰哥的文章：https://juejin.cn/post/7169033859894345765?searchId=20231106134913E2A625617FC73FDA0275
 */
class ScriptHelperService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected: ")
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onInterrupt() {

    }
}