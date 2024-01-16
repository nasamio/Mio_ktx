package com.mio.base.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.mio.base.Tag.TAG

abstract class BaseRvAdapter<B, T : ViewDataBinding>(
    val data: MutableList<B> = mutableListOf(),
    private val itemLayoutId: Int,
) : RecyclerView.Adapter<BaseRvAdapter.BaseRvViewHolder>() {
    class BaseRvViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        val binding = DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent.context),
            itemLayoutId,
            parent,
            false
        )
        return BaseRvViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        val binding = DataBindingUtil.bind<T>(holder.itemView)!!
        bindData(binding, data[position], position)
    }

    abstract fun bindData(binding: T, bean: B?, position: Int)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<B>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}