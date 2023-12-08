package com.mio.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mio.compose.bean.BottomNavItem
import kotlinx.coroutines.CoroutineScope


/**
 * 脚手架的使用demo
 */
@Preview
@Composable
fun ScaffoldEx(
    tabs: List<BottomNavItem> = listOf(), // 底部导航栏
    hasLogin: Boolean = false, // 是否登录

    coroutinesScope: CoroutineScope = rememberCoroutineScope(), // 协程作用域
    tabClickListener: (Int) -> Unit = {}, // 底部导航栏点击事件
    navigationClickListener: () -> Unit = {}, // 导航栏点击事件
    searchListener: () -> Unit = {}, // 搜索点击事件
    menuClickListener: (Int) -> Unit = {}, // 菜单点击事件
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, tab ->
                    BottomNavigationItem(
                        icon = {
                            val isSelected = selectedTabIndex == index
                            val iconModifier = Modifier
                                .padding(4.dp)
                                .size(if (isSelected) 32.dp else 24.dp)
                                .clip(MaterialTheme.shapes.small)

                            Image(
                                painterResource(tab.icon),
                                contentDescription = null,
                                modifier = iconModifier,
                                colorFilter = ColorFilter.tint(
                                    if (isSelected) Color.White
                                    else LocalContentColor.current
                                )
                            )
                        },
                        label = { Text(tab.title) },
                        selected = selectedTabIndex == index,
                        onClick = {
                            if (selectedTabIndex != index) {
                                selectedTabIndex = index
                                tabClickListener(index)
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (selectedTabIndex) {
            0 -> HomeContent(modifier)
            1 -> ChatContent(modifier)
            2 -> ProfileContent(modifier)
            3 -> WorkContent(modifier)
            4 -> SettingsContent(modifier)
            5 -> CartContent(modifier)
        }
    }

    // 如果没有登录，显示登录弹窗
    if (!hasLogin) {
        LoginDialog()
    }
}


@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    // Your Home content goes here
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Text(
            text = "Home Content",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ChatContent(modifier: Modifier = Modifier) {
    // Your Chat content goes here
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Text(
            text = "Chat Content",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ProfileContent(modifier: Modifier = Modifier) {
    // Your Profile content goes here
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Text(
            text = "Profile Content",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun WorkContent(modifier: Modifier = Modifier) {
    // Your Work content goes here
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Text(
            text = "Work Content",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    // Your Settings content goes here
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Text(
            text = "Settings Content",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun CartContent(modifier: Modifier = Modifier) {
    // Your Cart content goes here
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Magenta)
    ) {
        Text(
            text = "Cart Content",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
