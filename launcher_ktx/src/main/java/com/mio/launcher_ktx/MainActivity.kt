package com.mio.launcher_ktx

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lxj.xpopup.XPopup
import com.mio.base.BaseActivity
import com.mio.base.setClickListener
import com.mio.launcher_ktx.bean.AppInfo
import com.mio.launcher_ktx.databinding.ActivityMainBinding
import com.mio.launcher_ktx.databinding.ItemAppBinding
import com.mio.launcher_ktx.vm.MainViewModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    val mainViewModel by lazy { MainViewModel(application) }

    val mainAdapter by lazy {
        object : BaseQuickAdapter<AppInfo, BaseDataBindingHolder<ItemAppBinding>>(
            R.layout.item_app, mutableListOf()
        ) {
            override fun convert(holder: BaseDataBindingHolder<ItemAppBinding>, item: AppInfo) {
                holder.dataBinding?.apply {
                    ivItem.setImageDrawable(item.appIconDrawable)
                    tvItem.text = item.appName
                    root.setClickListener {
                        mainViewModel.startApp(item.pkgName)
                    }

                    val showAppDialog = XPopup.Builder(this@MainActivity)
                        .watchView(root)
                        .asAttachList(
                            arrayOf("隐藏", "卸载", "分享", "查看源码", "打开", "删除"),
                            null
                        ) { _, text ->
                            when (text) {
                                "隐藏" -> { // 先触发动画 然后再去删除真实数据 数据更新会触发界面的刷新，但此时界面的数据已经变化了
                                    removeOnlyUi(item) {
                                        mainViewModel.hideApp(item)
                                    }

                                }

                                "卸载" -> { // 这个得先触发卸载逻辑 然后监听广播 之后删除数据
                                    mainViewModel.uninstallApp(item)
                                }
                            }
                        }
                    root.setOnLongClickListener {
                        showAppDialog.show()
                        true
                    }
                }
            }


        }
    }

    override fun initView() {
        mDataBinding.rv.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(this@MainActivity, mainViewModel.showCount.value!!)
        }
    }

    override fun initObserver() {
        initPermission()
        initBroadcast()
        mainViewModel.apply {
            appList.observe(
                this@MainActivity
            ) { value ->
                mainAdapter.setNewData(value.toMutableList())
            }
            showCount.observe(
                this@MainActivity
            ) { value ->
                mDataBinding.rv.layoutManager = GridLayoutManager(this@MainActivity, value)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun initData() {
        lifecycleScope.launch(Dispatchers.IO) {
            mainViewModel.scanAppList()

        }
    }

    fun removeOnlyUi(item: AppInfo, func: () -> Unit = {}) {
        lifecycleScope.launch(Dispatchers.Main) {
            mainAdapter.remove(item)
            delay(1000)
            func()
        }
    }

    private fun initPermission() {
        AndPermission.with(this@MainActivity)
            .runtime()
            .permission(
                arrayOf(
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    "android.permission.REQUEST_DELETE_PACKAGES",
                )
            ).onGranted {

            }.onDenied {

            }.start()
    }

    /**
     * 初始化广播监听 监听应用安装和卸载事件
     */
    private fun initBroadcast() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addDataScheme("package")
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    intent.data?.schemeSpecificPart?.let { pkg ->
                        val info =
                            packageManager.getApplicationInfo(pkg, PackageManager.GET_META_DATA)
                        val appInfo = AppInfo(
                            pkgName = info.packageName,
                            appName = info.loadLabel(packageManager).toString(),
                            appIconDrawable = info.loadIcon(packageManager),
                        )
                        when (intent.action) {
                            Intent.ACTION_PACKAGE_ADDED -> {
                                mainViewModel.addApp2List(appInfo)
                            }

                            Intent.ACTION_PACKAGE_REMOVED -> {
                                removeOnlyUi(appInfo) {

                                }
                            }
                        }
                    }
                }
            }
        }, filter)
    }


}
