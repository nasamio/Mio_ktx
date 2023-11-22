package com.mio.account.fragment

import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mio.account.App
import com.mio.account.MainActivity
import com.mio.account.R
import com.mio.account.adapter.ItemBean
import com.mio.account.adapter.MainAdapter
import com.mio.account.bean.User
import com.mio.account.databinding.FragmentMineBinding
import com.mio.account.databinding.ItemTitleMenuBinding
import com.mio.account.databinding.ItemUserBinding
import com.mio.account.utils.SharedPreferencesHelper
import com.mio.base.BaseFragment
import com.mio.base.Tag.TAG
import com.mio.base.addChangeCallback
import com.mio.base.dp
import com.mio.base.extension.bg
import com.mio.base.extension.bgOvalAndNormal
import com.mio.base.extension.colorDrawable
import com.mio.base.extension.toBean
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MineFragment : BaseFragment<FragmentMineBinding>(R.layout.fragment_mine) {
    private val rvAdapter: MainAdapter by lazy {
        MainAdapter()
    }


    override fun initView() {
        // 透明状态栏
        (mContext as MainActivity).window.statusBarColor =
            mContext.getColor(R.color.bg_green)
        // 初始化rv
        mDataBinding.rv.apply {
            layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
            adapter = rvAdapter
            bgOvalAndNormal(
                mContext.getColor(com.mio.base.R.color.black_30),
                mContext.getColor(R.color.bg_green),
                400.dp, factor = 1.5f,
            )
        }

    }

    override fun initObserver() {
    }

    override fun initData() {

        rvAdapter.setNewInstance(
            mutableListOf<ItemBean>(
                ItemBean(ItemBean.USER),
                ItemBean(ItemBean.VIP),
                ItemBean(ItemBean.MENU4, R.menu.mine_menu),
                ItemBean(ItemBean.MENU_TAB, R.menu.menu_tab),
            )
        )
    }
}

