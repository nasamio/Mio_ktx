package com.mio.account.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mio.account.App
import com.mio.account.R
import com.mio.account.adapter.ItemBean.Companion.MENU4
import com.mio.account.adapter.ItemBean.Companion.MENU_TAB
import com.mio.account.adapter.ItemBean.Companion.TITLE_MENU
import com.mio.account.adapter.ItemBean.Companion.USER
import com.mio.account.adapter.ItemBean.Companion.VIP
import com.mio.account.bean.User
import com.mio.account.databinding.ItemItemTitleMenuBinding
import com.mio.account.databinding.ItemMineMenuBinding
import com.mio.account.databinding.ItemMineTabBinding
import com.mio.account.databinding.ItemRvMenuBinding
import com.mio.account.databinding.ItemTitleMenuBinding
import com.mio.account.databinding.ItemUserBinding
import com.mio.account.databinding.ItemVipBinding
import com.mio.account.net.NetHelper
import com.mio.account.utils.DialogHelper
import com.mio.base.Tag.TAG
import com.mio.base.addChangeCallback
import com.mio.base.dp
import com.mio.base.extension.toBean
import com.mio.base.extension.toJson
import com.mio.base.margin
import com.mio.base.parseMenu
import com.mio.base.setClickListener
import com.mio.base.setGridRvItemDecoration
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class MainAdapter : BaseMultiItemQuickAdapter<ItemBean, BaseViewHolder>() {

    init {
        addItemType(TITLE_MENU, R.layout.item_title_menu)
        addItemType(USER, R.layout.item_user)
        addItemType(VIP, R.layout.item_vip)
        addItemType(MENU4, R.layout.item_rv_menu)
        addItemType(MENU_TAB, R.layout.item_rv_menu)
    }

    val viewMap: MutableMap<Int, View> by lazy { mutableMapOf() }

    override fun convert(holder: BaseViewHolder, item: ItemBean) {
        val position = holder.adapterPosition
        when (holder.itemViewType) {
            TITLE_MENU -> bindTitleMenu(
                ItemTitleMenuBinding.bind(holder.itemView),
                item, position
            )

            USER -> bindUser(
                ItemUserBinding.bind(holder.itemView),
                item, position
            )

            VIP -> bindVip(
                ItemVipBinding.bind(holder.itemView),
                holder,
                item, position
            )

            MENU4 -> bindMenu4(
                ItemRvMenuBinding.bind(holder.itemView),
                holder,
                item, position
            )

            MENU_TAB -> bindMenuTab(
                ItemRvMenuBinding.bind(holder.itemView),
                holder,
                item, position
            )
        }

        if (holder.adapterPosition == itemCount - 1) {
            holder.itemView.margin(bottom = 20.dp)
        }
    }

    private fun bindMenuTab(
        binding: ItemRvMenuBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        adapterPosition: Int
    ) {
        val rvAdapter: BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineTabBinding>> by lazy {
            object :
                BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineTabBinding>>(R.layout.item_mine_tab) {
                override fun convert(
                    holder: BaseDataBindingHolder<ItemMineTabBinding>,
                    item: MenuItem
                ) {
                    holder.dataBinding?.apply {
                        icItem.setImageDrawable(item.icon)
                        tvItem.text = item.title
                        root.setClickListener {

                        }
                    }
                }
            }
        }
        binding.rvMenu.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        GlobalScope.launch(Dispatchers.IO) {
            val tab = context.parseMenu(item.menuResId)
            withContext(Dispatchers.Main) {
                rvAdapter.setList(tab.children.toList())
            }
        }
    }

    private fun bindMenu4(
        binding: ItemRvMenuBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        adapterPosition: Int
    ) {
        val rvAdapter: BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineMenuBinding>> by lazy {
            object :
                BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineMenuBinding>>(R.layout.item_mine_menu) {
                override fun convert(
                    holder: BaseDataBindingHolder<ItemMineMenuBinding>,
                    item: MenuItem
                ) {
                    holder.dataBinding?.apply {
                        icItem.setImageDrawable(item.icon)
                        tvItem.text = item.title
                        root.setClickListener {

                        }
                    }
                }
            }
        }
        binding.rvMenu.apply {
            adapter = rvAdapter
            layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        }
        GlobalScope.launch(Dispatchers.IO) {
            val tab = context.parseMenu(item.menuResId)
            withContext(Dispatchers.Main) {
                rvAdapter.setList(tab.children.toList())
            }
        }
    }

    private fun bindVip(
        binding: ItemVipBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        adapterPosition: Int
    ) {

    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun bindUser(binding: ItemUserBinding, item: ItemBean, adapterPosition: Int) {
        App.hasLogin.addChangeCallback(true) { login ->
            Log.d(TAG, "bindUser: $login")
            binding.apply {
                if (login) {
                    GlobalScope.launch {
                        val user = App.sharedPreferencesHelper.getString("user", "")
                            ?.toBean(User::class.java)
                        withContext(Dispatchers.Main) {
                            user?.let {
                                binding.tvUserName.text = it.name
                                binding.tvUserId.text = "ID: ${it.id}"
                                binding.tvDuration.text = "${it.useDays()}"
                            }
                        }
                    }
                } else {
                    binding.tvUserName.text = "未登录"
                    binding.tvUserId.text = "点击立即登录"
                }
            }
        }

        binding.root.setClickListener {
            if (!App.hasLogin.get()) {
                DialogHelper.showLoginDialog(context) { name, pwd ->
                    // Log.d(TAG, "bindUser: name: $name, pwd: $pwd")
                    GlobalScope.launch(Dispatchers.IO) {
                        NetHelper.apiService.login(
                            User(name, pwd)
                        ).getHandledData()?.let {
                            App.sharedPreferencesHelper.saveString("user", it.toJson())
                            App.hasLogin.set(true)
                        }
                    }
                }
            }
        }

        // 看看有没有用户信息的缓存 如果有就直接显示
        GlobalScope.launch {
            App.sharedPreferencesHelper.getString("user", "")
                ?.toBean(User::class.java)?.let {
                    App.hasLogin.set(true)
                }
        }
    }

    private fun bindTitleMenu(binding: ItemTitleMenuBinding, item: ItemBean, position: Int) {
        binding.apply {
            Log.d(TAG, "bindTitleMenu: ${item.title}")
            tvTitle.text = item.title
            tvSub.text = item.subTitle

            val rvAdapter = object :
                BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemItemTitleMenuBinding>>(R.layout.item_item_title_menu) {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun convert(
                    holder: BaseDataBindingHolder<ItemItemTitleMenuBinding>,
                    item: MenuItem
                ) {
                    holder.dataBinding?.let { innerBinding ->
                        innerBinding.icItem.setImageDrawable(item.icon)
                        innerBinding.tvItemTitle.text = item.title
                        innerBinding.tvItemSub.text = item.contentDescription.toString()
                        innerBinding.root.setClickListener {
                            listener?.onItemClick(this@MainAdapter, it, position)
                        }
                    }
                }
            }
            rv.adapter = rvAdapter
            rv.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            rv.setGridRvItemDecoration(20.dp, onlyInner = true)
            GlobalScope.launch(Dispatchers.IO) {
                context.parseMenu(item.menuResId)
                withContext(Dispatchers.Main) {
                    val list = context.parseMenu(item.menuResId).children.toMutableList()
                    rvAdapter.setNewInstance(list)
                    list.forEach {
                        Log.d(TAG, "bindTitleMenu: ${it.title}")
                    }
                }
            }
        }
    }
}

var listener: OnItemClickListener? = null

public interface OnItemClickListener {
    fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: android.view.View, position: Int)
}

data class ItemBean(
    override val itemType: Int,
    var menuResId: Int = 0,
    var title: String = "",
    var subTitle: String = "",
) : MultiItemEntity {
    companion object {
        const val TITLE_MENU = 0
        const val USER = 1
        const val VIP = 2
        const val MENU4 = 3
        const val MENU_TAB = 4
    }
}