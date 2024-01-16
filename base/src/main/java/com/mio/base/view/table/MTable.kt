package com.mio.base.view.table

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.base.BaseView
import com.mio.base.R
import com.mio.base.Tag.TAG
import com.mio.base.databinding.ItemCellBinding
import com.mio.base.databinding.ItemTableBinding
import com.mio.base.databinding.ViewMtableBinding
import com.mio.base.dp
import com.mio.base.extension.roundedDrawable
import com.mio.base.extension.shapeDrawable
import com.mio.base.margin
import com.mio.base.setClickListener
import com.mio.base.toast
import com.mio.base.utils.getProperty
import com.mio.base.utils.setProperty
import com.mio.base.view.adapter.BaseRvAdapter

class MTable<B : Any>(
    context: Context,
    attributeSet: AttributeSet?,
) : BaseView<ViewMtableBinding>(
    context, attributeSet, R.layout.view_mtable
) {
    private val columnList = mutableListOf<Column>() // 表头

    val rvAdapter = object : BaseRvAdapter<B, ItemTableBinding>(
        mutableListOf(), R.layout.item_table
    ) {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun bindData(binding: ItemTableBinding, bean: B?, position: Int) {
//            Log.d(TAG, "bindData: $position")
            binding.llRoot.removeAllViews()
            columnList.forEach { column ->
                binding.llRoot.addView(
                    when (column.type) {
                        CellType.Color -> {
                            ColorCircleView(
                                context,
                                requireColor(bean?.getProperty(column.property) as Int)
                            ).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    column.width,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )
                                background = context.getDrawable(R.drawable.bg_cell_text)
                                foreground = context.getDrawable(R.drawable.fg_ripple_no_corner)

                                setClickListener {
                                    context.toast("点击了$position")
                                }
                            }
                        }

                        CellType.Image -> {
                            ImageView(context).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    column.width,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                                )
                                background = context.getDrawable(R.drawable.bg_cell_text)
                                foreground = context.getDrawable(R.drawable.fg_ripple_no_corner)
                                setImageResource(
                                    if (bean?.getProperty(column.property) as Boolean) {
                                        R.drawable.lock
                                    } else {
                                        R.drawable.unlock
                                    }
                                )
                                setOnClickListener {
                                    context.toast("点击了$position")
                                    val lock = bean.getProperty(column.property) as Boolean
                                    bean.setProperty(column.property, !lock)
                                    setImageResource(
                                        if (bean.getProperty(column.property) as Boolean) {
                                            R.drawable.lock
                                        } else {
                                            R.drawable.unlock
                                        }
                                    )
                                }
                            }
                        }

                        else -> { // 默认文本
                            TextView(context).apply {
                                text = bean?.getProperty(column.property).toString()
                                gravity = android.view.Gravity.CENTER
                                background = context.getDrawable(R.drawable.bg_cell_text)
                                layoutParams = LinearLayout.LayoutParams(
                                    column.width,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                            }
                        }
                    }
                )
            }

            // 点击事件
            binding.llRoot.setClickListener {

            }
        }
    }

    private fun requireColor(property: Int): Int {
        return when (property % 4) {
            1 -> Color.RED
            2 -> Color.BLUE
            3 -> Color.GREEN
            else -> Color.BLACK
        }
    }

    override fun initView() {
        Log.d(TAG, "initView: ")
        mDataBinding.rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)

        }
        setBackgroundColor(Color.parseColor("#42C0FB"))
    }

    fun setData(data: List<B>) {
        if (data.isEmpty()) showEmpty()
        if (columnList.isEmpty()) initBean(data[0])
        rvAdapter.setData(data)
        // 更新标题栏
        updateTitle()
    }

    private fun updateTitle() {
        mDataBinding.llTitle.removeAllViews()
        columnList.forEach {
            mDataBinding.llTitle.addView(
                TextView(context).apply {
                    text = it.title
                    gravity = android.view.Gravity.CENTER
                    background = context.getDrawable(R.drawable.bg_cell_text)
                    layoutParams = LinearLayout.LayoutParams(
                        it.width,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        }
    }

    private fun showEmpty() {

    }

    private fun initBean(bean: B) {
        // 获取类的注解 以获取表对应的属性 如表标题等
        bean::class.annotations.find { it.annotationClass == MioBean::class }?.let {
            val classAnnotation = it as MioBean
            val title = classAnnotation.title
            Log.d(TAG, "initBean: title: $title")
        }
        // 获取对象中的所有字段 及 对应的注解和值
        bean::class.members.forEach { member ->
            member.annotations.find { it.annotationClass == MioColumn::class }
                ?.let { // 没有添加注解的属性会在这里自动排除
                    val mioColumn = it as MioColumn
                    Log.d(TAG, "initBean: ${mioColumn.title} ${member.name} ${member.call(bean)}")
                    columnList.add(
                        Column(
                            mioColumn.index,
                            mioColumn.title,
                            member.name,
                            mioColumn.width,
                            mioColumn.type
                        )
                    )
                }
        }
        columnList.sortBy { it.index }
    }

    data class Column(
        val index: Int,
        val title: String,
        val property: String,
        val width: Int = 50.dp,
        val type: CellType = CellType.NormalText,
    )
}