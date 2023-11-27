package com.mio.base.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class RvLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean = false) :
    LinearLayoutManager(context, orientation, false) {
    var isScrollEnabled = true
    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnabled
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && isScrollEnabled
    }
}