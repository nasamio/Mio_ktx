package com.mio.account.utils

import android.content.Context
import com.lxj.xpopup.XPopup
import com.mio.account.adapter.listener
import com.mio.account.view.LoginXpop

object DialogHelper {
    fun showLoginDialog(context: Context, l: (String, String) -> Unit) {
        XPopup.Builder(context)
            .asCustom(LoginXpop(context, l))
            .show()
    }
}