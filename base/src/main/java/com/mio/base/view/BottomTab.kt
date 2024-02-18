package com.mio.base.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEachIndexed
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableInt
import com.mio.base.R
import com.mio.base.addChangeCallback
import com.mio.base.bgColor
import com.mio.base.databinding.ItemBtvBinding
import com.mio.base.dp
import com.mio.base.parseMenu
import com.mio.base.playTogether
import com.mio.base.scaleXx
import com.mio.base.scaleYy
import com.mio.base.size
import com.mio.base.topMargin
import kotlin.math.min

class BottomTab(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private val TAG = "BottomTab"
    var menuId: Int = -1 // menu id
        set(value) {
            if (value != field) {
                field = value
                removeAllViews()
                addItemView(value)
            }
        }
    lateinit var menu: Menu // 加载的item

    var itemTopMargin = 20.dp // item 上边距
    var itemMarginBetweenImg2Text = 20.dp // 图和文字的间距

    var imgFactor = .3f // 图的边长系数 是item的长宽中较短的乘系数
    var textsize = 12.dp.toFloat() // 文字大小
    var checkPos = ObservableInt(0) // 默认选择的
    var scaleFactor = 1.2f // 选择的时候的缩放系数

    // 背景暂时无效
    var normalBgColor: Int = context.getColor(R.color.white_30)
    var checkedBgColor: Int = context.getColor(R.color.white_50)

    var onCheckChange: OnCheckChangeListener? = null
    var callbackImmediately = true

    val lastBgColorMap = mutableMapOf<Int, Int>() // index:color

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BottomTab)
        ta.apply {
            itemTopMargin =
                ta.getDimension(R.styleable.BottomTab_topMargin, itemTopMargin.toFloat()).toInt()
            itemMarginBetweenImg2Text = ta.getDimension(
                R.styleable.BottomTab_marginBetweenImg2Text,
                itemMarginBetweenImg2Text.toFloat()
            ).toInt()
            imgFactor = ta.getDimension(R.styleable.BottomTab_imgFactor, imgFactor)
            textsize = ta.getDimension(R.styleable.BottomTab_textSize, textsize)
            checkPos.set(ta.getInt(R.styleable.BottomTab_defaultCheckPos, checkPos.get()))
            scaleFactor = ta.getFloat(R.styleable.BottomTab_scaleFactor, scaleFactor)
            callbackImmediately =
                ta.getBoolean(R.styleable.BottomTab_callbackImmediately, callbackImmediately)

            if (menuId == -1) {
                menuId = ta.getResourceId(R.styleable.BottomTab_menu, 0)
            }
            addItemView(menuId)
            ta.recycle()
        }

        checkPos.addChangeCallback { onCheckChange?.let { l -> l.onChange(it) } }
    }

    private fun addItemView(menuId: Int) {
        if (menuId != 0) {
            menu = context.parseMenu(menuId)
            val inflater = LayoutInflater.from(context)
            removeAllViews()
            menu.forEachIndexed { index, item ->
                Log.d(TAG, "$index: $item")
                val view = inflater.inflate(R.layout.item_btv, this@BottomTab, false)
                bindView(view, item, index)
                addView(view)
            }
//            setBackgroundColor(normalBgColor)
        } else {
            Log.e(TAG, "no menu found... ")
        }
    }

    private fun bindView(view: View, item: MenuItem, index: Int) {
        val binding = ItemBtvBinding.bind(view)
        binding.ic.setImageDrawable(item.icon)
        binding.tvTitle.text = item.title

        binding.ic.topMargin(itemTopMargin)
        binding.tvTitle.topMargin(itemMarginBetweenImg2Text)

        val isCheck = index == checkPos.get()
        check(binding, isCheck, index)
        if (callbackImmediately && isCheck) {
            postDelayed({ onCheckChange?.onChange(index) }, 300)
        }

        checkPos.addChangeCallback {
            check(binding, index == it, index)
        }

        view.setOnClickListener {
            checkPos.set(index)
        }
    }

    private fun check(binding: ItemBtvBinding, check: Boolean, index: Int) {
        val scale = if (check) scaleFactor else 1f
        val startColor = lastBgColorMap[index] ?: 0
        Log.d(TAG, "check: start:$startColor,normal:$normalBgColor")
        val bgColor = if (check) checkedBgColor else normalBgColor
        val tf = if (check) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        binding.tvTitle.typeface = tf
        lastBgColorMap[index] = bgColor
        context.playTogether(
//            view.scaleXx(scale),
//            view.scaleYy(scale),
            binding.root.bgColor(bgColor, startColor = startColor),
            binding.ic.scaleXx(scale),
            binding.ic.scaleYy(scale),
            binding.tvTitle.scaleXx(scale),
            binding.tvTitle.scaleYy(scale),
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val pWidth = MeasureSpec.getSize(widthMeasureSpec)

        forEachIndexed { index, view ->
            val cWidth =
                pWidth * 1f / childCount// pw = cw * count + margin * (count + 1)
            val childWidthMeasureSpec =
                MeasureSpec.makeMeasureSpec(cWidth.toInt(), MeasureSpec.EXACTLY)
            val childHeightMeasureSpec =
                getChildMeasureSpec(heightMeasureSpec, 0, view.layoutParams.height)
            view.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            // 这里来自动设置一下图标的大小 如果大小不为0 会跳过
            setItemContentSize(view)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        forEachIndexed { index, view ->
            val dw = view.measuredWidth

            view.layout(
                index * dw,
                0,
                (index + 1) * dw,
                view.measuredHeight
            )
        }
    }

    private fun setItemContentSize(view: View) {
        val binding = DataBindingUtil.getBinding<ItemBtvBinding>(view)
        val mw = view.measuredWidth
        val mh = view.measuredHeight
        val a = min(mw, mh)

        binding?.apply {
            ic.size((a * imgFactor).toInt(), (a * imgFactor).toInt())
            tvTitle.textSize = textsize
        }
    }

    fun setCheckedChangeListener(listener: OnCheckChangeListener) {
        onCheckChange = listener
    }

    public interface OnCheckChangeListener {
        fun onChange(pos: Int)
    }
}