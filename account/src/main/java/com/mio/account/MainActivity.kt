package com.mio.account

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mio.account.bean.User
import com.mio.account.fragment.MainFragment
import com.mio.account.fragment.MineFragment
import com.mio.account.fragment.MoreFragment
import com.mio.account.fragment.StatisticsFragment
import com.mio.account.net.NetHelper
import com.mio.base.BaseFragmentActivity
import com.mio.base.Tag.TAG
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
        }
    }
}