package com.mio.mio_ktx.ui

import android.util.Log
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.dp
import com.mio.base.md5
import com.mio.base.px
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentABinding

class AFragment : BaseFragment<FragmentABinding>(R.layout.fragment_a) {
    override fun initView() {
        mDataBinding.btn.setOnClickListener {
            val activity = activity as MainActivity
            activity.replaceFragment(
                R.id.container, activity.fragments[1],
                TRANSIT_FRAGMENT_OPEN,
                animatorEnter = R.anim.fragment_enter_from_right,
                animatorOut = R.anim.fragment_exit_to_left,
            )
        }

        // 测试 px/dp
        val num = 100
        Log.d(
            TAG, "initView:" +
                    "${num}px : ${num.px}," +
                    "${num}dp : ${num.dp}"
        )

        // 测试 md5
        val text = "abc"
        Log.d(TAG, "initView: text: $text , md5: ${text.md5()}")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}