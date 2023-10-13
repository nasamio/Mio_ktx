package com.mio.mio_ktx.ui

import androidx.fragment.app.FragmentTransaction
import com.mio.base.BaseFragment
import com.mio.base.replaceFragment
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentABinding
import com.mio.mio_ktx.databinding.FragmentBBinding

class BFragment : BaseFragment<FragmentBBinding>(R.layout.fragment_b) {
    override fun initView() {
        mDataBinding.btn.setOnClickListener {
            val activity = activity as MainActivity
            activity.replaceFragment(
                R.id.container, activity.fragments[0],
                FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
                animatorEnter = R.anim.fragment_enter_from_left,
                animatorOut = R.anim.fragment_exit_to_right,
            )
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}