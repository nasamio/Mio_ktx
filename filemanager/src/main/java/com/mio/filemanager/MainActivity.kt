package com.mio.filemanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.mio.base.Tag.TAG
import com.mio.filemanager.helper.KtorHelper
import com.mio.filemanager.helper.SftpHelper
import com.mio.filemanager.ui.FileGrid
import com.mio.filemanager.ui.FileShow
import com.mio.filemanager.ui.theme.Mio_ktxTheme
import com.mio.filemanager.viewmodel.FileShowViewModel
import com.mio.filemanager.viewmodel.SshViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val sshViewModel by viewModels<SshViewModel>()
    private val fileShowViewModel by viewModels<FileShowViewModel>()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mio_ktxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting(sshViewModel)
                    FileShow(fileShowViewModel)

                }
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
//            fileShowViewModel.list("/home/ubuntu/file/")
//            sshViewModel.connect()
        }

        lifecycleScope.launch(Dispatchers.IO) {
//            SshjHelper.apply {
//                connect()
//                delay(1000)
//                listDirectory("file/")
//            }
//            JschUtil(
//                "117.50.190.141",
//                22,
//                "ubuntu",
//                "Qq123456"
//            ).apply {
//                connect()
//                delay(1000)
//                execCommand("cd file && ls")
//                execCommand("cd file/")
//                execCommand("ls")
//            }

//            val list = KtorHelper.list("/home/ubuntu/file/")
//            Log.d(TAG, "onCreate: list: $list")

//            testJsch()
        }
    }

    private suspend fun testJsch() {
        SftpHelper.apply {
            connect(
                connectRes = {
                    Log.d(TAG, "testJsch: connectRes: $it")
                },
                execRes = {
                    Log.d(TAG, "testJsch: execRes: $it")
                }
            )

            execCommandByShell("ls")
            exec("cd file && ls")
//
//            Log.d(TAG, "testJsch: before")
//            exec("cd file && ls")
//            Log.d(TAG, "testJsch: exec: cd")
//            delay(1000)
//            exec("ls")
//            Log.d(TAG, "testJsch: exec: ls")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        SftpHelper.session.disconnect()
    }
}

@SuppressLint(
    "UnrememberedMutableState", "CoroutineCreationDuringComposition",
    "StateFlowValueCalledInComposition"
)
@Composable
fun Greeting(vm: SshViewModel, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    val text = vm.textFlow.collectAsState()
    val lastLine = vm.lastLine.collectAsState()
    var input by remember { mutableStateOf("") }

    // Create a FocusRequester
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Text(
            text = text.value,
            modifier = Modifier.fillMaxWidth(),
        )
        // 添加一个输入框
//        TextField(
//            value = lastLine.value, // value应该是text.value的最后一行的内容
//            onValueChange = { newValue ->
//                vm.changeInput(newValue)
//                focusRequester.requestFocus()
//            },
//            modifier = Modifier
//                .focusRequester(focusRequester) // Attach the FocusRequester to the TextField
//                .onGloballyPositioned { focusRequester.requestFocus() }, // Request focus when the TextField is first drawn
//            // 监听回车事件
//            keyboardActions = KeyboardActions(onDone = {
//
//            })
//        )

        TextField(
            value = TextFieldValue(
                lastLine.value,
                selection = TextRange(maxOf(vm.normalInput.length, vm.lastLine.value.length))
            ),
            onValueChange = { newValue ->
                if (newValue.text.endsWith("\n")) {
                    Log.d(TAG, "Greeting: 回车...:${newValue.text}")
                    vm.send(newValue.text)
                    return@TextField
                }

                vm.changeInput(newValue.text)
                focusRequester.requestFocus()
            },
            modifier = Modifier
                .focusRequester(focusRequester) // Attach the FocusRequester to the TextField
                .onGloballyPositioned { focusRequester.requestFocus() }, // Request focus when the TextField is first drawn
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    Mio_ktxTheme {
//        Greeting("Android")
//    }
}