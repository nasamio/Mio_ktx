package com.mio.base.view.table

import androidx.databinding.ViewDataBinding
import com.mio.base.R
import com.mio.base.databinding.ItemViewTableBinding
import com.mio.base.view.adapter.BaseRvAdapter

class MioTableAdapter(data: List<Any>) : BaseRvAdapter<Any, ItemViewTableBinding>(
    mutableListOf(), R.layout.item_view_table
) {
    override fun bindData(binding: ItemViewTableBinding, bean: Any?, position: Int) {

    }

}