package com.mio.launcher_ktx.vm

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mio.base.toast
import com.mio.launcher_ktx.bean.AppInfo
import com.mio.launcher_ktx.utils.ShellUtils

@SuppressLint("QueryPermissionsNeeded")

class MainViewModel(val app: Application) : AndroidViewModel(app) {
    private val packageManager: PackageManager by lazy { getApplication<Application>().packageManager }

    val appList = MutableLiveData<MutableList<AppInfo>>(mutableListOf())
    val showCount = MutableLiveData(4)

    suspend fun scanAppList() {
        packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .also { // 去除系统应用
                it.removeIf {
                    it.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM != 0
                }
            }
            .map { // 映射成自定义对象
                AppInfo(
                    pkgName = it.packageName,
                    appName = it.loadLabel(packageManager).toString(),
                    appIconDrawable = it.loadIcon(packageManager),
                )
            }
            .toMutableList() // 转换成可变数组
            .let {
                appList.postValue(it)
            }
    }

    fun startApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            getApplication<Application>().startActivity(it)
        }
    }

    /**
     * 从列表中隐藏应用
     */
    fun hideApp(item: AppInfo) {
        appList.postValue(appList.value?.filter { it.pkgName != item.pkgName } as MutableList<AppInfo>?)
    }

    /**
     * 添加应用到列表 会去重
     */
    fun addApp2List(appInfo: AppInfo) {
        appList.value?.find { it.pkgName == appInfo.pkgName }
            ?: run { appList.postValue(appList.value?.plus(appInfo) as MutableList<AppInfo>?) }
    }

    fun uninstallApp(item: AppInfo) {
        app.toast("卸载应用:${item.appName},pkg name:${item.pkgName}")

        ShellUtils.execCmd(
            "pm uninstall ${item.pkgName}"
        )

    }
}