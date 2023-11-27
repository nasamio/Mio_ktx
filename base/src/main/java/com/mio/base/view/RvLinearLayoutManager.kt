package com.mio.base.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class RvLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean = false) :
    LinearLayoutManager(context, orientation, false) {
    var isScrollEnabled = true
    var canScrollHorizontal = true
    var canScrollVertical = true
    override fun canScrollVertically(): Boolean {
        return canScrollHorizontal && isScrollEnabled
    }

    override fun canScrollHorizontally(): Boolean {
        return canScrollVertical && isScrollEnabled
    }
}