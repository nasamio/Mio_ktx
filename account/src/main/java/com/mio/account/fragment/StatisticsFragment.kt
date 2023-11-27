package com.mio.account.fragment

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.account.R
import com.mio.account.adapter.ItemBean
import com.mio.account.adapter.MainAdapter
import com.mio.account.databinding.FragmentStatiticsBinding
import com.mio.base.BaseFragment
import com.mio.base.view.RvLinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StatisticsFragment : BaseFragment<FragmentStatiticsBinding>(R.layout.fragment_statitics) {
    private val rvAdapter: MainAdapter by lazy {
        MainAdapter()
    }

    override fun initView() {
        mDataBinding.rv.apply {
            adapter = rvAdapter
            layoutManager = RvLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
        lifecycleScope.launch {
            delay(300)
            rvAdapter.setNewInstance(
                mutableListOf(
                    ItemBean(
                        ItemBean.TITLE,
                        "统计",
                    ),
                    ItemBean(
                        ItemBean.TITLE_CUSTOM_CAT,
                        R.menu.menu_wlgl,
                        "分类统计",
                        "-8944 11月·支出"
                    ),
                    ItemBean(
                        ItemBean.TITLE_CUSTOM_TREND,
                        R.menu.menu_wlgl,
                        "日趋势",
                        "-2123 11月22日·支出"
                    ),
                    ItemBean(
                        ItemBean.TITLE_CUSTOM_BAR,
                        R.menu.menu_wlgl,
                        "月度收支",
                        "-8323 11月·支出"
                    ),
                )
            )
        }
    }

}