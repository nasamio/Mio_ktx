package com.mio.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 快速 fragment vp
 */
class BaseQuickFragmentVpAdapter(
    fragmentManager: FragmentManager,
    val data: MutableList<Fragment> = mutableListOf(),
) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Fragment = data[position]
}