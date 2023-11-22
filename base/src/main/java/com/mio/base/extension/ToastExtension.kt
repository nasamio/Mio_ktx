package com.mio.base.extension

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun Context.toast(text: String) {
    shortToast(text)
}


fun Context.shortToast(text: String) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(this@shortToast, text, Toast.LENGTH_SHORT).show()
    }
}

fun Context.longToast(text: String) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(this@longToast, text, Toast.LENGTH_LONG).show()
    }
}