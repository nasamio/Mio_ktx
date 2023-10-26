package com.mio.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 快速使用 仅支持一种item的vp2的基于data binding 的基类
 */
abstract class BaseVp2QuickAdapter<B : Any, T : ViewDataBinding>(
    private val itemLayoutId: Int,
    val data: MutableList<B> = mutableListOf()
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent.context),
            itemLayoutId,
            parent,
            false
        )
        return QuickViewHolder(itemBinding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        DataBindingUtil.bind<T>(holder.itemView)?.let {
            bind(it, position)
        }
    }

    class QuickViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 绑定子布局
     */
    abstract fun bind(bind: T, position: Int)
}
