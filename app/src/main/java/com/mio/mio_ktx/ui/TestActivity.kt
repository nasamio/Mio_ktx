package com.mio.mio_ktx.ui

import com.mio.base.BaseActivity
import com.mio.base.databinding.ItemCellBinding
import com.mio.base.setClickListener
import com.mio.base.view.adapter.BaseRvAdapter
import com.mio.base.view.table.User
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ActivityTestBinding
import com.mio.mio_ktx.databinding.ItemTestBinding

class TestActivity : BaseActivity<ActivityTestBinding>(R.layout.activity_test) {
    val rvAdapter = object : BaseRvAdapter<User, ItemCellBinding>(
        mutableListOf(), com.mio.base.R.layout.item_cell
    ) {
        override fun bindData(binding: ItemCellBinding, bean: User?, position: Int) {
//            binding.tv.text = bean?.name
        }
    }

    override fun initView() {
        mDataBinding.rv.apply {
            adapter = rvAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        }

        mDataBinding.mtv.setData(
            getTestData()
        )
//        mDataBinding.rv.visibility = android.view.View.VISIBLE
//        mDataBinding.mtv.visibility = android.view.View.GONE
//        mDataBinding.rv.postDelayed(
//            {
//                rvAdapter.setData(
//                    getTestData()
//                )
//            }, 3000
//        )
    }

    override fun initObserver() {
    }

    override fun initData() {
    }

    fun getTestData(): MutableList<User> {
        return mutableListOf<User>().apply {
            repeat(100) {
                add(
                    User(
                        it, "name$it", "pwd$it", "男", "desp",
                        color = it, isLock = it % 2 == 0
                    )
                )
            }
        }
    }
}