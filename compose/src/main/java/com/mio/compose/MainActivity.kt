package com.mio.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mio.base.Tag.TAG
import com.mio.compose.ui.BottomNavItem
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
    }
}

@Composable
@Preview
fun BottomNavigationExample() {
    ScaffoldEx(
        tabs = listOf(
            BottomNavItem("Home", Icons.Filled.Home),
            BottomNavItem("Chat", Icons.Filled.Mail),
            BottomNavItem("Profile", Icons.Filled.Face),
            BottomNavItem("Work", Icons.Filled.Work),
            BottomNavItem("Settings", Icons.Filled.Settings),
            BottomNavItem("Cart", Icons.Filled.ShoppingCart),
        ), tabClickListener = {
            Log.d(TAG, "BottomNavigationExample onTabClick: $it")
        }
    )
}
