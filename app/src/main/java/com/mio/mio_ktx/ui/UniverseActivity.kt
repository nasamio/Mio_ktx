package com.mio.mio_ktx.ui

import android.util.AttributeSet
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mio.base.BaseActivity
import com.mio.base.dp
import com.mio.base.setClickListener
import com.mio.base.view.u.Circle
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ActivityUniverseBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random

/**
 * 哈哈
 */
class UniverseActivity : BaseActivity<ActivityUniverseBinding>(R.layout.activity_universe) {
    override fun initView() {
        mDataBinding.fab.setClickListener {
//            mDataBinding.vpUniverse.isRunning
//            if (mDataBinding.vpUniverse.isRunning) {
//                mDataBinding.vpUniverse.stop()
//            } else {
//                mDataBinding.vpUniverse.start()
//            }
        }

        lifecycleScope.launch {
            delay(1000)
            val random = Random()
            repeat(12) {
                val circle = Circle(this@UniverseActivity, null)
                val layoutParams = ViewGroup.LayoutParams(
                    30.dp, 30.dp
                )
                circle.layoutParams = layoutParams

                circle.cx = 30.dp / 2.0 + random.nextInt(mDataBinding.root.width - 30.dp).toDouble()
                circle.cy =
                    30.dp / 2.0 + random.nextInt(mDataBinding.root.height - 30.dp).toDouble()
                circle.mass = 1.0
                circle.vx = 1000.0
                circle.vy = 1000.0
                circle.e = 0.8
                circle.color = if (it % 2 == 0) {
                    com.mio.base.R.color.black_30
                } else {
                    com.mio.base.R.color.lcv_check
                }

                val speed = 80.0
                // 测试横向
//                circle.cx = if (it % 2 == 0) 500.0 else 50.0
//                circle.cy = 100.0
//                circle.vx = if (it % 2 == 0) -speed else speed
//                circle.vy = 0.0

                // 测试纵向
//                circle.cx = 100.0
//                circle.cy = if (it % 2 == 0) 1000.0 else 50.0
//                circle.vx = 0.0
//                circle.vy = if (it % 2 == 0) -speed else speed


                mDataBinding.vpUniverse.addView(circle)

            }
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}