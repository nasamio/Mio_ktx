package com.mio.music

import MusicManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.mio.base.BaseActivity
import com.mio.base.BaseQuickFragmentVpAdapter
import com.mio.base.Tag.TAG
import com.mio.base.addOnPageSelectListener
import com.mio.music.databinding.ActivityMainBinding
import com.mio.music.databinding.LayoutMiniPlayerBinding
import com.mio.music.ui.MainFragment
import com.mio.music.ui.MineFragment
import com.mio.music.ui.ToolsFragment
import com.mio.music.ui.VideoFragment
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

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

    private lateinit var miniBinding: LayoutMiniPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        checkPermission()

        super.onCreate(savedInstanceState)
    }

    private fun checkPermission() {

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

//        miniBinding = DataBindingUtil.bind(mDataBinding.mini.root)!!
//        miniBinding.apply {
//            imgPlay.setOnClickListener {
//                MusicManager.apply {
//                    if (isPlaying()) pauseMusic() else resumeMusic()
//                }
//            }
//            imgNext.setOnClickListener { MusicManager.playNext() }
//            imgPrevious.setOnClickListener { MusicManager.playPrevious() }
//        }
//
//        MusicManager.setPlayingStatusCallback {
//            miniBinding.imgPlay.setImageResource(
//                if (it) R.drawable.ic_pause
//                else R.drawable.ic_play
//            )
//        }
//
//        MusicManager.setProgressCallback {
////            Log.d(TAG, "initView: progress: $it")
//
//            setProgress(it)
//        }
//        MusicManager.initialize()

    }

    private fun setProgress(p: Float) {
        miniBinding.vProgress.progress = (p * 100f).toInt()
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}