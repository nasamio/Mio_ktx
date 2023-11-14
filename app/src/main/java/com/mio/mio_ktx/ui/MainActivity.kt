package com.mio.mio_ktx.ui

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.mio.base.BaseActivity
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val fragments: List<BaseFragment<out ViewDataBinding>> by lazy {
        listOf(
            AFragment(),
            BFragment(),
        )
    }

    override fun initView() {
        showInitTag = true

        lifecycleScope.launch {
//            showLoading()
//            delay(1_200)
            replaceFragment(R.id.container, AFragment())
//            showContent()
        }

    }

    override fun initData() {
    }

    override fun initObserver() {
    }

}