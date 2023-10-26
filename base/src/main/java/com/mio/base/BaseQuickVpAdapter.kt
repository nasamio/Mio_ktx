package com.mio.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter

/**
 * 快速vp
 */
abstract class BaseQuickVpAdapter<B : Any, T : ViewDataBinding>(
    private val itemLayoutId: Int,
    val data: MutableList<B> = mutableListOf(),
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemBinding = DataBindingUtil.inflate<T>(
            LayoutInflater.from(container.context),
            itemLayoutId,
            container,
            false
        )

        bind(itemBinding, position)

        container.addView(itemBinding.root)
        return itemBinding.root
    }

    override fun getCount(): Int = data.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    abstract fun bind(itemBinding: ViewDataBinding, position: Int)
}
