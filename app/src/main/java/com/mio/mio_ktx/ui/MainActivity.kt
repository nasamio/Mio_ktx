package com.mio.mio_ktx.ui

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.mio.base.BaseActivity
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val fragments: List<BaseFragment<out ViewDataBinding>> by lazy {
        listOf(
            AFragment(),
            BFragment(),
        )
    }

    override fun initView() {
        showInitTag = true

        replaceFragment(R.id.container, AFragment())

    }

    override fun initData() {
    }

    override fun initObserver() {
    }

}