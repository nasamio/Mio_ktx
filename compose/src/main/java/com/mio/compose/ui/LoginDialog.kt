package com.mio.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mio.compose.App
import com.mio.compose.net.KtorHelper
import com.mio.compose.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
@Preview
fun LoginDialog(
    onDismissRequest: () -> Unit = {},
) {
    var isPasswordLogin by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = {

    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .background(Color.White)
        ) {
            // 切换登录方式按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 根据登录方式显示对应文本和处理点击事件
                Text(
                    text = if (!isPasswordLogin) "密码登录" else "验证码登录",
                    modifier = Modifier
                        .clickable { isPasswordLogin = !isPasswordLogin }
                        .padding(8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                )
                // 暂不登录
                Text(
                    text = "暂不登录",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable {
                            // 处理暂不登录的逻辑
                        },
                    fontSize = TextUnit(12f, TextUnitType.Sp),
                    color = Color.Gray,
                )
            }

            // 输入框和按钮
            if (isPasswordLogin) {
                // 密码登录
                EditText(
                    value = username,
                    label = "用户名",
                    onValueChange = { username = it },
                    icon = Icons.Default.Lock
                )

                EditText(
                    value = password,
                    label = "密码",
                    onValueChange = { password = it },
                    icon = Icons.Default.Lock
                )
            } else {
                // 验证码登录
                EditText(
                    value = phoneNumber,
                    label = "手机号",
                    onValueChange = { phoneNumber = it },
                    icon = Icons.Default.Lock
                )

                EditText(
                    value = verificationCode,
                    label = "验证码",
                    onValueChange = { verificationCode = it },
                    icon = Icons.Default.Send,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(end = 80.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    // 发送验证码按钮
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                if (phoneNumber.length == 11) {
                                    val loginBean = KtorHelper.sendYzm(phoneNumber)
                                    withContext(Dispatchers.Main) {
                                        val msg =
                                            if (loginBean.code == 200) "发送成功" else "发送失败"
                                        toast(msg)
                                    }
                                } else {
                                    toast("请检查手机号是否正确")
                                }
                            }
                        }
                    ) {
                        Text("发送验证码")
                    }
                }
            }

            // 登录按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        // 处理登录操作
                        if (isPasswordLogin) {
                            // 密码登录
                        } else {
                            // 验证码登录
                            coroutineScope.launch {
                                val loginBean =
                                    KtorHelper.loginWithYzm(phoneNumber, verificationCode)
                                if (loginBean.code == 200) {
//                                    App.loginType = loginBean
                                    // todo 完善登录逻辑
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .width(100.dp)
                ) {
                    Text("登录")
                }
            }
        }
    }
}


@Composable
fun SnackbarComponent(
    onCloseSnackbar: () -> Unit,
    message: String,
) {
    var visibility by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(true) }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            visibility = true
            delay(2000L) // 2 seconds delay
            showSnackbar = false
        }
    }

    if (visibility) {
        Snackbar(
            action = {
                TextButton(onClick = { showSnackbar = false }) {
                    Text("Dismiss")
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(message, color = Color.White)
        }
    }
}


