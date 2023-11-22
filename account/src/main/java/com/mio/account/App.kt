package com.mio.account

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.mio.account.utils.SharedPreferencesHelper

object App {
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    fun init(context: Context) {
        sharedPreferencesHelper = SharedPreferencesHelper(context)
        hasLogin.set(sharedPreferencesHelper.getBoolean("hasLogin", false))
    }

    val hasLogin: ObservableBoolean by lazy {
        ObservableBoolean()
    }
}