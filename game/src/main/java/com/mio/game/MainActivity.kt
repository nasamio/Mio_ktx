package com.mio.game

import com.mio.base.BaseActivity
import com.mio.game.databinding.ActivityMainBinding
import com.mio.game.view.DirController

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun initView() {
        mDataBinding.dc.onDirChangeListener = object : DirController.OnDirChangeListener {
            override fun onChange(dx: Float, dy: Float) {
//                Log.d(TAG, "onChange: $x , $y")
                mDataBinding.fv.user.apply {
                    val speed = speed * .1f * (1000L / mDataBinding.dc.callbackDuration)

                    x += (dx * speed).toInt()
                    y += (dy * speed).toInt()
                    mDataBinding.fv.invalidate()
                }
            }
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}