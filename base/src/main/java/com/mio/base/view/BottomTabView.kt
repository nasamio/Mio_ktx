package com.mio.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import androidx.core.view.size
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mio.base.BaseView
import com.mio.base.R
import com.mio.base.databinding.ItemBtvBinding
import com.mio.base.databinding.LayoutBtvBinding
import com.mio.base.parseMenu

@SuppressLint("RestrictedApi", "Recycle")
class BottomTabView(context: Context, attr: AttributeSet) :
    FrameLayout(context, attr) {
    private val TAG = "BottomTabView"
    var checkPos = 0
    var margin = 0
    lateinit var menu: Menu

    init {
//        val ta = context.obtainStyledAttributes(attr, R.styleable.BottomTabView)
//        checkPos = ta.getInt(R.styleable.BottomTabView_defaultCheckPos, 0)
//        Log.d(TAG, "check pos: $checkPos")
//        val menuId = ta.getResourceId(R.styleable.BottomTabView_menu, 0)
//        if (menuId != 0) {
//            menu = context.parseMenu(menuId)
//        } else {
//            Log.e(TAG, "no menu found... ")
//        }
//        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        Log.d(TAG, "onMeasure: w mode:$wMode,h mode:$hMode,w:$w,h:$h")

        forEachIndexed { index, view ->
            view.measure(
                MeasureSpec.makeMeasureSpec(
                    (widthMeasureSpec * 1f / childCount).toInt(), MeasureSpec.EXACTLY
                ), heightMeasureSpec
            )
        }

        setMeasuredDimension(w, h)
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

//        testAddView()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        addView()

        children.forEachIndexed { index, view ->
            Log.d(TAG, "onLayout: index:$index,view width:${view.measuredWidth}")
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    private fun addView() {
        val inflater = LayoutInflater.from(context)
        menu.forEachIndexed { index, item ->
            Log.d(TAG, "$index: $item")
            val view = inflater.inflate(R.layout.item_btv, this, false)
//            view.setBackgroundColor(context.getColor(R.color.white))
            val lp = view.layoutParams as LayoutParams
            lp.width =
                (width - (menu.size + 1) * margin) / menu.size // item宽度*item数+margin*(item数+1） = width
            Log.d(TAG, "onSizeChanged: big width:$width,item width:${lp.width}")
            view.layoutParams = lp
            Log.d(TAG, "addView: view width:${view.measuredWidth}")
//            view.left = index * lp.width
//            view.top = 0

            bindView(view, item)
            addView(view)
//            view.layout(index * lp.width, 0, (index + 1) * lp.width, height)
        }
    }

    private fun testAddView() {
        val v2 = View(context)
        v2.layoutParams = LinearLayout.LayoutParams(100, 100)
        v2.setBackgroundColor(context.getColor(R.color.white))
        addView(v2)
    }

    private fun bindView(view: View?, item: MenuItem) {
        view?.let {
            val binding = ItemBtvBinding.bind(it)
            Log.d(TAG, "bindView: title:${item.title}")
            binding.ic.setImageDrawable(item.icon)
            binding.tvTitle.text = item.title
        }
    }

    fun initView() {

    }
}