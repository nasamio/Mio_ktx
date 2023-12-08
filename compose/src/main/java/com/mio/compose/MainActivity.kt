package com.mio.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mio.base.Tag.TAG
import com.mio.base.toast
import com.mio.base.utils.LiveDataBus
import com.mio.compose.bean.BottomNavItem
import com.mio.compose.ui.ScaffoldEx
import com.mio.compose.ui.theme.Mio_ktxTheme

/**
 * 最核心的是：数据向下传递，事件向上传递
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mio_ktxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting()
//                    JetpackCompose()
//                    HomeScreen()

                    BottomNavigationExample()
                }
            }
        }

        test()
    }

    private fun test() {
        LiveDataBus.get().with("toast", String::class.java).observe(this) {
            toast(it)
        }
    }
}

fun toast(text: String) {
    LiveDataBus.get().with("toast").postValue(text)
}

@Composable
@Preview
fun BottomNavigationExample() {
    ScaffoldEx(
        tabs = listOf(
            BottomNavItem("发现", R.drawable.ic_discovery),
            BottomNavItem("播客", R.drawable.ic_podcast),
            BottomNavItem("我的", R.drawable.ic_mine),
            BottomNavItem("关注", R.drawable.ic_sing),
            BottomNavItem("社区", R.drawable.ic_cloud_country),
        ), tabClickListener = {
            Log.d(TAG, "BottomNavigationExample onTabClick: $it")
        }
    )
}
