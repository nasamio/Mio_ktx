package com.mio.music

import androidx.core.view.forEachIndexed
import androidx.core.view.get
import com.mio.base.BaseActivity
import com.mio.base.BaseQuickFragmentVpAdapter
import com.mio.base.addOnPageSelectListener
import com.mio.music.databinding.ActivityMainBinding
import com.mio.music.ui.MainFragment
import com.mio.music.ui.MineFragment
import com.mio.music.ui.ToolsFragment
import com.mio.music.ui.VideoFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val fragmentVpAdapter: BaseQuickFragmentVpAdapter by lazy {
        BaseQuickFragmentVpAdapter(
            supportFragmentManager, mutableListOf(
                MainFragment(),
                VideoFragment(),
                ToolsFragment(),
                MineFragment(),
            )
        )
    }

    override fun initView() {
        mDataBinding.vp.apply {
            this.adapter = fragmentVpAdapter
            addOnPageSelectListener {
                mDataBinding.bnv.selectedItemId = mDataBinding.bnv.menu[it].itemId
            }
        }

        mDataBinding.bnv.setOnNavigationItemSelectedListener {
            mDataBinding.bnv.menu.forEachIndexed { index, item ->
                if (it.itemId == item.itemId) {
                    mDataBinding.vp.setCurrentItem(index, true)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }


    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}