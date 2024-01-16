package com.mio.filemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mio.base.Tag.TAG
import com.mio.filemanager.helper.SftpHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SshViewModel : ViewModel() {

    // 界面文字 flow
    val textFlow = MutableStateFlow("")
    val lastLine = MutableStateFlow("")
    var normalInput = ""

    fun connect() {
        textFlow.value = " connect..."
        SftpHelper.connect(
            connectRes = {
                textFlow.value += "\n connect: $it"

                viewModelScope.launch(Dispatchers.IO) {
                    SftpHelper.execCommandByShell("ls")
                }
            },
            execRes = {
                if (it.isNotEmpty()) {
                    if (it.contains("@") && it.contains(":") && it.contains("$")) {
                        lastLine.value = it.trim()
//                        Log.d(TAG, "connect: match: $it")
                        normalInput = it.trim()
                    } else {
                        textFlow.value += "\n ${it.trim()}"
//                        Log.d(TAG, "connect: no match: $it")
                    }
                }
            }
        )

    }

    fun changeInput(newValue: String) {
        Log.d(TAG, "changeInput: newValue: $newValue")
        if (newValue.length < normalInput.length) {
            lastLine.value = normalInput
        } else {
            lastLine.value = newValue
        }
    }

    fun send(text: String) {
        val realText = text.replace(normalInput, "")
        SftpHelper.execCommandByShell(realText)

    }
}