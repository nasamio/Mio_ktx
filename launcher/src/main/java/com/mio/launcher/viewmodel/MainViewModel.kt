package com.mio.launcher.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mio.base.Tag.TAG
import com.mio.launcher.bean.AppInfo
import com.mio.launcher.bean.CommandResult
import com.mio.launcher.utils.CmdUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {
    // 应用列表的集合
    val appList = MutableStateFlow<MutableList<AppInfo>>(mutableListOf())

    @SuppressLint("QueryPermissionsNeeded")
    fun scanAppList() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "scanAppList: start")
            // 获取应用列表
            appList.value.clear()
            val pm = getApplication<Application>().packageManager
            val installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            installedApplications.forEach {
//                Log.d(TAG, "scanAppList: ${it.packageName}")

                // 如果是系统应用 直接return
                if (it.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM != 0) {
                    return@forEach
                }

                appList.value.add(
                    AppInfo(
                        name = it.loadLabel(pm).toString(),
                        packageName = it.packageName,
                        icon = it.icon,
                        iconDrawable = it.loadIcon(pm)
                    )
                )
            }

            sort()

            Log.d(TAG, "scanAppList: size: ${appList.value.size}")
        }
    }

    private fun sort() {
        // 排序一下 如果包含nav就排在前面 如果包含mio也放前面 但是nav优先级高
        val newList = appList.value.toMutableList()
        newList.sortByDescending {
            if (it.packageName.contains("hzz", ignoreCase = true)) {
                4
            } /*else if (it.name.contains("Nav", ignoreCase = true)) {
                3
            }*/ else if (it.name.contains("mio", ignoreCase = true)) {
                2
            } else {
                1
            }
        }
        appList.value = newList
    }

    fun startApp(packageName: String) {
        Log.d(TAG, "startApp: $packageName")
        val intent =
            getApplication<Application>().packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            getApplication<Application>().startActivity(it)
        }
//        val isRoot: Boolean = CmdUtils.isRoot()
//
//        val command =
//            "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* am start -n $packageName"
//        val commandResult: CommandResult =
//            CmdUtils.execCmd(arrayOf<String>(command), isRoot, true)!!
//        Log.d(TAG, "startApp: $commandResult")
    }

    fun uninstall(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val uninstallSilent = uninstallSilent(packageName)
//            Log.d(TAG, "uninstall: $uninstallSilent,app size:${appList.value.size}")
            if (uninstallSilent) {
                val newList =
                    appList.value.filter { it.packageName != packageName }.toMutableList()
                appList.value = newList
            }
//            Log.d(TAG, "uninstall: app size:${appList.value.size}")
        }
    }


    /**
     * 卸载应用成功&失败
     */
    fun uninstallSilent(packageName: String, isKeepData: Boolean = false): Boolean {
        val isRoot: Boolean = CmdUtils.isRoot()
        val command =
            "LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall " + (if (isKeepData) "-k" else "") + packageName
        val commandResult: CommandResult =
            CmdUtils.execCmd(arrayOf<String>(command), isRoot, true)!!
        return (commandResult.successMsg != null
                && commandResult.successMsg.toLowerCase(Locale.ROOT).contains("success"))
    }

}