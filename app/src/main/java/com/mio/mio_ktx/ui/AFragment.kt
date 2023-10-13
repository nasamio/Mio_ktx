package com.mio.mio_ktx.ui

import android.util.Log
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentABinding

class AFragment : BaseFragment<FragmentABinding>(R.layout.fragment_a) {
    override fun initView() {
        Log.d(TAG, "initView: $this")

        mDataBinding.btn.setOnClickListener {
            val activity = activity as MainActivity
            activity.replaceFragment(
                R.id.container, activity.fragments[1],
                TRANSIT_FRAGMENT_OPEN,
                animatorEnter = R.anim.fragment_enter_from_right,
                animatorOut = R.anim.fragment_exit_to_left,
            )
        }
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