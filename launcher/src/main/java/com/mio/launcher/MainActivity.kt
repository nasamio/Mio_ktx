package com.mio.launcher

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.mio.base.Tag.TAG
import com.mio.launcher.bean.AppInfo
import com.mio.launcher.ui.theme.Mio_ktxTheme
import com.mio.launcher.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel by lazy { MainViewModel(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        mainViewModel.scanAppList()
        setContent {
            Mio_ktxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainList(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun MainList(viewModel: MainViewModel) {
    // 列表展示 每行四个 可以滚动
    val appList = viewModel.appList.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.train),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4), // 设置每行四个item
        ) {
            items(appList.value.size) { index ->
                AppItem(viewModel, appList.value[index])
            }
        }
    }
}

@Composable
fun AppItem(viewModel: MainViewModel, appInfo: AppInfo, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(DpOffset.Zero) }
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .height(100.dp)
            .clickable {
                // 点击事件 跳转到应用详情
                viewModel.startApp(appInfo.packageName)
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { pos ->
                        position = with(density) { DpOffset(pos.x.toDp(), pos.y.toDp() - 100.dp) }
                        expanded = true
                    },
                    onTap = {
                        viewModel.startApp(appInfo.packageName)
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(10.dp))
        Icon(
            modifier = modifier.size(50.dp),
            // 加载drawable appInfo.iconDrawable
            painter = BitmapPainter(appInfo.iconDrawable!!.toBitmap().asImageBitmap()),
            contentDescription = null,
            tint = Color.Unspecified  // 取消默认的色调模式
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = if (appInfo.name.length > 10) "${
                appInfo.name.substring(
                    0,
                    10
                )
            }..." else appInfo.name,
            color = Color.White
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = position
        ) {
            DropdownMenuItem(text = { Text("卸载") }, onClick = {
                viewModel.uninstall(appInfo.packageName)
                expanded = false
            })
            DropdownMenuItem(text = { Text("应用详情") }, onClick = {
                // 显示详情
                expanded = false
            })
        }
    }


}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mio_ktxTheme {
        Greeting("Android")
    }
}