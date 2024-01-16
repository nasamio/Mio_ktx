package com.mio.filemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mio.base.Tag.TAG
import com.mio.filemanager.bean.RFile
import com.mio.filemanager.helper.ErrorHelper
import com.mio.filemanager.helper.KtorHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FileShowViewModel : ViewModel() {
    val showContent = MutableStateFlow("FileShow")
    val path = MutableStateFlow("")
    val files = MutableStateFlow(mutableListOf<RFile>())
    val tips = MutableStateFlow("")
    val showHiddenFiles = MutableStateFlow(false)

    suspend fun list(path: String) {
//        KtorHelper.list(path).data.let {
//            files.value = it
//        }
        this.path.value = path

        flow {
            KtorHelper.list(path).data.let {
                emit(it)
                tips.value = if (it.isEmpty()) "空" else "加载成功"
            }
        }.catch {
            emit(listOf())
            tips.value = "加载失败，请检查网络"
            ErrorHelper.post(it)
        }.collect {
            // 把data按照名字排序
            files.value = it.toMutableList().apply {
                // 去除前面的文件夹目录
                forEach { file ->
                    file.name = file.path.substringAfterLast("/")
                }
                // 去除隐藏文件
                if (!showHiddenFiles.value) {
                    removeAll { file -> file.name.startsWith(".") }
                }

                // 排序
                sortBy { file -> file.path }
            }
        }
    }

    suspend fun click(file: RFile) {
        if (file.isDir) {
            list(file.path)
        } else {
            Log.d(TAG, "click: 打开文件: ${file.path}")
        }
    }

    suspend fun back() {
        // 如果path中的 / 小于等于一个 说明已经到了根目录 不能返回了
        if (path.value.count { it == '/' } <= 1) {
            return
        }

        // list 当前目录的上一级目录
        list(path.value.substringBeforeLast("/"))
    }

    fun showImage(file: RFile) {
        path.value = file.path
        showContent.value = "ImageShow"
    }
}