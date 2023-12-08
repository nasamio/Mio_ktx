package com.mio.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
)

/**
 * 脚手架的使用demo
 */
@Composable
fun ScaffoldEx(
    tabs: List<BottomNavItem> = listOf(),
    coroutinesScope: CoroutineScope = rememberCoroutineScope(),
    tabClickListener: (Int) -> Unit = {},
    navigationClickListener: () -> Unit = {},
    searchListener: () -> Unit = {},
    menuClickListener: (Int) -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(
                            text = tabs[selectedTabIndex].title,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigationClickListener() }) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { searchListener() }) {
                        Icon(Icons.Filled.Search, contentDescription = null)
                    }
                    IconButton(onClick = {
                        // todo show menu
                    }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, tab ->
                    BottomNavigationItem(
                        icon = { Icon(tab.icon, contentDescription = null) },
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
