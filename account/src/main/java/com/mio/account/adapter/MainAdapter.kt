package com.mio.account.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
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
import com.mio.account.adapter.ItemBean.Companion.TITLE
import com.mio.account.adapter.ItemBean.Companion.TITLE_CUSTOM_BAR
import com.mio.account.adapter.ItemBean.Companion.TITLE_CUSTOM_CAT
import com.mio.account.adapter.ItemBean.Companion.TITLE_CUSTOM_TREND
import com.mio.account.adapter.ItemBean.Companion.TITLE_MENU
import com.mio.account.adapter.ItemBean.Companion.USER
import com.mio.account.adapter.ItemBean.Companion.VIP
import com.mio.account.bean.User
import com.mio.account.databinding.ItemBarBinding
import com.mio.account.databinding.ItemCatBinding
import com.mio.account.databinding.ItemItemTitleMenuBinding
import com.mio.account.databinding.ItemMineMenuBinding
import com.mio.account.databinding.ItemMineTabBinding
import com.mio.account.databinding.ItemRvMenuBinding
import com.mio.account.databinding.ItemTitleBinding
import com.mio.account.databinding.ItemTitleMenuBinding
import com.mio.account.databinding.ItemTrendBinding
import com.mio.account.databinding.ItemUserBinding
import com.mio.account.databinding.ItemVipBinding
import com.mio.account.net.NetHelper
import com.mio.account.utils.DialogHelper
import com.mio.base.Tag.TAG
import com.mio.base.addChangeCallback
import com.mio.base.dp
import com.mio.base.extension.normalTime
import com.mio.base.extension.toBean
import com.mio.base.extension.toJson
import com.mio.base.margin
import com.mio.base.parseMenu
import com.mio.base.setClickListener
import com.mio.base.setGridRvItemDecoration
import com.mio.base.utils.RvHelper
import com.mio.base.view.RingGraphView
import com.mio.base.view.RvLinearLayoutManager
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
        addItemType(TITLE_CUSTOM_CAT, R.layout.item_cat)
        addItemType(TITLE_CUSTOM_TREND, R.layout.item_trend)
        addItemType(TITLE_CUSTOM_BAR, R.layout.item_bar)
        addItemType(TITLE, R.layout.item_title)
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

            TITLE_CUSTOM_CAT -> {
                bindCat(
                    ItemCatBinding.bind(holder.itemView),
                    holder,
                    item, position
                )
            }

            TITLE_CUSTOM_TREND -> {
                bindTrend(
                    ItemTrendBinding.bind(holder.itemView),
                    holder,
                    item, position
                )
            }

            TITLE_CUSTOM_BAR -> {
                bindBar(
                    ItemBarBinding.bind(holder.itemView),
                    holder,
                    item, position
                )
            }

            TITLE -> {
                bindTitle(
                    ItemTitleBinding.bind(holder.itemView),
                    holder,
                    item, position
                )
            }
        }

        if (holder.adapterPosition == itemCount - 1) {
            holder.itemView.margin(bottom = 20.dp)
        }
    }

    private fun bindTitle(
        binding: ItemTitleBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        position: Int,
    ) {
        binding.tvTitle.text = item.title
    }

    private fun bindBar(
        binding: ItemBarBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        position: Int,
    ) {
        binding.rgv.apply {
            realStartAngle = 55f
            centerText = "全部 \n" + "-2313"
            data = mutableListOf(
                RingGraphView.RingData(57.6f, "餐饮"),
                RingGraphView.RingData(21.6f, "运动"),
                RingGraphView.RingData(13f, "日用"),
                RingGraphView.RingData(7.8f, "购物"),
            )
        }
        binding.tvTitle.text = item.title
        binding.tvSubTitle.text = item.subTitle
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindTrend(
        binding: ItemTrendBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        position: Int,
    ) {
        binding.tvTitle.text = item.title
        binding.tvSubTitle.text = item.subTitle

        // 实现横向拖动的时候 不让竖着的rv滚动
        val layoutManager = recyclerView.layoutManager as RvLinearLayoutManager
        binding.lcv.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    layoutManager.isScrollEnabled = false
                }

                MotionEvent.ACTION_UP -> {
                    layoutManager.isScrollEnabled = true
                }
            }
            false
        }
    }

    private fun bindCat(
        binding: ItemCatBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        position: Int,
    ) {
        binding.rgv.apply {
            realStartAngle = 55f
            centerText = "全部 \n" + "-2313"
            data = mutableListOf(
                RingGraphView.RingData(57.6f, "餐饮"),
                RingGraphView.RingData(21.6f, "运动"),
                RingGraphView.RingData(13f, "日用"),
                RingGraphView.RingData(7.8f, "购物"),
            )
        }
        binding.tvTitle.text = item.title
        binding.tvSubTitle.text = item.subTitle
    }

    private fun bindMenuTab(
        binding: ItemRvMenuBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        adapterPosition: Int,
    ) {
        val rvAdapter: BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineTabBinding>> by lazy {
            object :
                BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineTabBinding>>(R.layout.item_mine_tab) {
                override fun convert(
                    holder: BaseDataBindingHolder<ItemMineTabBinding>,
                    item: MenuItem,
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
        adapterPosition: Int,
    ) {
        val rvAdapter: BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineMenuBinding>> by lazy {
            object :
                BaseQuickAdapter<MenuItem, BaseDataBindingHolder<ItemMineMenuBinding>>(R.layout.item_mine_menu) {
                override fun convert(
                    holder: BaseDataBindingHolder<ItemMineMenuBinding>,
                    item: MenuItem,
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

    @SuppressLint("SetTextI18n")
    private fun bindVip(
        binding: ItemVipBinding,
        holder: BaseViewHolder,
        item: ItemBean,
        adapterPosition: Int,
    ) {
        App.hasLogin.addChangeCallback(true) { login ->
            binding.apply {
                if (login) {
                    GlobalScope.launch {
                        val user = App.sharedPreferencesHelper.getString("user", "")
                            ?.toBean(User::class.java)
                        withContext(Dispatchers.Main) {
                            user?.let {
                                binding.tvVip.text = "高级会员"
                                binding.tvTime.text =
                                    if (System.currentTimeMillis() > it.vipTime.normalTime()) {
                                        "已过期"
                                    } else {
                                        "${
                                            it.vipTime.substring(0, it.vipTime.indexOf("."))
                                                .replace("T", " ")
                                        }到期"
                                    }
                            }
                        }
                    }
                } else {
                    binding.tvVip.text = "普通用户"
                    binding.tvTime.text = "请先登录"
                }

                GlobalScope.launch(Dispatchers.Main) {
                    binding.tvGoTo.text =
                        if (login) "去续费" else "去登录"
                    binding.tvGoTo.setClickListener {
                        if (!App.hasLogin.get()) {
                            showLogin()
                        } else {
                            showXufei()
                        }
                    }
                }
            }
        }
    }

    /**
     * 续费
     */
    private fun showXufei() {

    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun bindUser(binding: ItemUserBinding, item: ItemBean, adapterPosition: Int) {
        App.hasLogin.addChangeCallback(true) { login ->
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
                showLogin()
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

    private fun showLogin() {
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
                    item: MenuItem,
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

    constructor(itemType: Int, title: String) : this(itemType, 0, title, "")

    companion object {
        const val TITLE_MENU = 0
        const val USER = 1
        const val VIP = 2
        const val MENU4 = 3
        const val MENU_TAB = 4
        const val TITLE_CUSTOM_CAT = 5
        const val TITLE_CUSTOM_TREND = 6
        const val TITLE_CUSTOM_BAR = 7
        const val TITLE = 8
    }
}