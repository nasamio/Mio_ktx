package com.mio.account.fragment

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.account.R
import com.mio.account.adapter.ItemBean
import com.mio.account.adapter.ItemBean.Companion.TITLE_MENU
import com.mio.account.adapter.MainAdapter
import com.mio.account.databinding.FragmentMoreBinding
import com.mio.base.BaseFragment
import com.mio.base.dp
import com.mio.base.extension.bg
import kotlinx.coroutines.launch

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {
    private val rvAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun initView() {
        mDataBinding.rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        }
        mDataBinding.tvMore.bg(mContext.getColor(R.color.bg_green), 10.dp.toFloat())
    }

    override fun initObserver() {
    }

    override fun initData() {
        lifecycleScope.launch {
            rvAdapter.setNewInstance(
                mutableListOf<ItemBean>(
                    ItemBean(TITLE_MENU, R.menu.menu_wlgl, "往来管理", "来而不往非礼也"),
                    ItemBean(TITLE_MENU, R.menu.menu_wlgl, "计划", "自动执行，记账更轻松"),
                )
            )
        }
    }
}