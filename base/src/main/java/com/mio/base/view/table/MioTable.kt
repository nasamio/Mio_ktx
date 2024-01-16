package com.mio.base.view.table

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.base.BaseView
import com.mio.base.R
import com.mio.base.Tag.TAG
import com.mio.base.databinding.ItemCellBinding
import com.mio.base.databinding.ItemViewTableBinding
import com.mio.base.databinding.ViewTableBinding
import com.mio.base.utils.getMembers
import com.mio.base.utils.getProperty
import com.mio.base.view.adapter.BaseRvAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ViewConstructor")
class MioTable<T : Any>(
    context: Context,
    attr: AttributeSet?,
) : BaseView<ViewTableBinding>(context, attr, R.layout.view_table) {
    val job = Job()
    val scope = CoroutineScope(Dispatchers.Main + job)

    val paint by lazy {
        Paint().apply {
            color = context.resources.getColor(R.color.black_80)
            strokeWidth = 1f
            isAntiAlias = true
        }
    }
    private val columnList = mutableListOf<MioColumn>() // 标题列

    private val rvAdapter = object : BaseRvAdapter<T, ItemViewTableBinding>(
        mutableListOf(), R.layout.item_view_table
    ) {
        override fun bindData(binding: ItemViewTableBinding, bean: T?, position: Int) {
            Log.d(TAG, "bindData: ")
            bindInnerRv(binding.rv, bean)
        }
    }

    fun setData(data: List<T>) {
        scope.launch(Dispatchers.IO) {
//            if (data.isEmpty()) showEmpty()
//            if (columnList.isEmpty()) initColumnList(data[0])

            withContext(Dispatchers.Main) {
                rvAdapter.setData(data)
            }
        }
    }

    /**
     * 初始化标题数据 这是个耗时过程
     */
    private fun initColumnList(t: T) {
        // 获取类的注解
        val annotation = t::class.annotations.find { it.annotationClass == MioBean::class }
        val title = annotation?.getMembers()?.find { it == "title" }
        Log.d(TAG, "initColumnList: title: $title")
        // 获取类的属性名
        t.getMembers().forEach {
            Log.d(TAG, "initColumnList properties: $it")
        }

        columnList.clear()
        // 获取类的属性的注解
        t::class.members.forEach { member ->
//            Log.d(TAG, "initColumnList: ${it.name}")
            member.annotations.find { it.annotationClass == MioColumn::class }
                ?.let { // 没有添加注解的属性会在这里自动排除
                    val mioColumn = it as MioColumn
                    columnList.add(
                        MioColumn(
                            index = mioColumn.index,
                            title = mioColumn.title,
                            type = mioColumn.type,
                            width = mioColumn.width,
                            propertyName = member.name,
                        )
                    )
                    columnList.sortBy { it.index }
                }
        }

        columnList.forEach {
            Log.d(TAG, "initColumnList: $it")
        }
    }

    private fun showEmpty() {

    }

    override fun initView() {
        Log.d(TAG, "initView: ")
        mDataBinding.rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
            // 设置边界线
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    super.onDraw(c, parent, state)
                    parent.children.last().let {
                        c.drawLine(
                            it.left.toFloat(),
                            it.bottom.toFloat(),
                            it.right.toFloat(),
                            it.bottom.toFloat(),
                            paint
                        )
                    }
                }
            })
        }

    }

    private fun bindInnerRv(rv: RecyclerView, t: T?) {
        rv.apply {
            adapter = object : BaseRvAdapter<String, ItemCellBinding>(
                columnList.map { it.propertyName }.toMutableList(),
                R.layout.item_cell
            ) {
                override fun bindData(binding: ItemCellBinding, bean: String?, position: Int) {
//                    binding.tv.text = t?.getProperty(bean!!).toString()
                }
            }
            // 横向的
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            // 设置边界线
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    super.onDraw(c, parent, state)
                    // 绘制边界线
                    Log.d(TAG, "onDraw: ${parent.layoutManager}")

                    for (i in 0 until parent.childCount) {
                        val child = parent.getChildAt(i)
                        val left = child.left
                        val right = child.right
                        val top = child.top
                        val bottom = child.bottom
                        c.drawLine(
                            left.toFloat(),
                            top.toFloat(),
                            right.toFloat(),
                            top.toFloat(),
                            paint
                        )
                        c.drawLine(
                            left.toFloat(),
                            bottom.toFloat(),
                            left.toFloat(),
                            top.toFloat(),
                            paint
                        )
                        c.drawLine(
                            right.toFloat(),
                            top.toFloat(),
                            right.toFloat(),
                            bottom.toFloat(),
                            paint
                        )

                    }

                }
            })
        }
    }

    data class ColumnBean(
        val index: Int, // 排序用的编号
        val title: String, // 标题 --> 列名
        val valueName: String, // 属性名
    )
}