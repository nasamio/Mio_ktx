package com.mio.filemanager.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mio.filemanager.R
import com.mio.filemanager.bean.RFile
import com.mio.filemanager.viewmodel.FileShowViewModel
import kotlinx.coroutines.launch

@Composable
fun FileShow(vm: FileShowViewModel) {
    val showContent = vm.showContent.collectAsState().value

    when (showContent) {
        "FileShow" -> FileShow(vm)
        "ImageShow" -> ImageShow(vm)
        else -> Text(text = "未知界面")
    }
}

/**
 * 底部 左侧是一个返回键 中间显示当前目录
 */
@Composable
fun FileTitle(
    vm: FileShowViewModel,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.fillMaxWidth()
        .height(50.dp)
//        .padding(10.dp)
        .background(
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1E90FF),
                    Color(0xFF00BFFF),
                ),
                start = Offset(0f, 0f),
                end = Offset(1000f, 0f),
            )
        ),
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true),
                    onClick = { scope.launch { vm.back() } }
                )
        )

        Text(
            text = vm.path.collectAsState().value,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun FileGrid(vm: FileShowViewModel) {
    val path = vm.path.collectAsState()
    val files = vm.files.collectAsState()
    val scope = rememberCoroutineScope()
    val tips = vm.tips.collectAsState()

    LaunchedEffect(Unit) {
        // 这个只有第一次加载才执行一次
        vm.list("/home/ubuntu/file/")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (files.value.isEmpty()) {
            Text(
                text = tips.value,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                columns = GridCells.Fixed(4),
            ) {
                items(files.value) { file ->
                    FileItem(
                        file = file,
                        path = path.value,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(150.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = true),
                                onClick = { scope.launch { vm.click(file) } }
                            )
                            .padding(4.dp),
                        vm
                    )
                }
            }
        }
    }
}

@Composable
fun FileItem(
    file: RFile,
    path: String,
    modifier: Modifier = Modifier,
    vm: FileShowViewModel,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(100.dp)
                .clickable {
                    vm.showImage(file)
                },
            painter = painterResource(
                id = if (file.isDir) R.drawable.explore else getFileImg(file.path)
            ),
            contentDescription = null
        )
        Text(
            text = file.name, // text内容是file.path从尾部去掉vm.file.value.path的部分
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

fun getFileImg(path: String): Int {
    return if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")) {
        R.drawable.img
    } else if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".rmvb")) {
        R.drawable.video
    } else if (path.endsWith(".mp3") || path.endsWith(".wav") || path.endsWith(".flac")) {
        R.drawable.music
    } else {
        R.drawable.unknown
    }
}

@Composable
fun ImageShow(vm: FileShowViewModel) {
    val path = vm.path.collectAsState()
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.img),
            contentDescription = null
        )
        Text(
            text = path.value,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}
