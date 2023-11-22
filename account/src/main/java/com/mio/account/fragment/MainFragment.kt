package com.mio.account.fragment

import com.mio.account.R
import com.mio.account.databinding.FragmentMainBinding
import com.mio.base.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun initView() {
        mDataBinding.root.setBackgroundColor(mContext.getColor(com.mio.base.R.color.black_30))
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}