package com.mio.base.view.table

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mio.base.BaseView
import com.mio.base.R
import com.mio.base.Tag.TAG
import com.mio.base.databinding.ItemCellBinding
import com.mio.base.databinding.ItemViewTableBinding
import com.mio.base.databinding.ViewTableBinding
import com.mio.base.utils.getAnnotationValue
import com.mio.base.utils.getMembers
import com.mio.base.utils.getProperty
import com.mio.base.view.adapter.BaseRvAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ViewConstructor")
class MioTableView<T : Any>(
    context: Context,
    attr: AttributeSet?,
) : BaseView<ViewTableBinding>(context, attr, R.layout.view_table) {
    val paint by lazy {
        Paint().apply {
            color = context.resources.getColor(R.color.black_80)
            strokeWidth = 1f
            isAntiAlias = true
        }
    }
    val columnList = mutableListOf<Temp>()

    var titleLayoutId: Int = 0
    var contentLayoutId: Int = 0
    val layoutInflater by lazy { LayoutInflater.from(context) }

    private val rvAdapter = object : BaseRvAdapter<User, ItemViewTableBinding>(
        mutableListOf(
            User(1, "张三", "男", "desp"),
            User(2, "李四", "男", "desp"),
            User(3, "王五", "男", "desp"),
            User(4, "李四", "女", "desp"),
            User(5, "王五", "女", "desp"),
            User(6, "王五", "女", "desp"),
        ), R.layout.item_view_table
    ) {
        override fun bindData(binding: ItemViewTableBinding, bean: User?, position: Int) {
            Log.d(TAG, "bindData: ")
            bindInnerRv(binding.rv, bean!!, members = mutableListOf())
        }
    }

    override fun initView() {
        Log.d(TAG, "initView: ")
        val data = mutableListOf<User>().apply {
            repeat(100) {
                add(User(it, "name$it", "pwd$it"))
            }
        }

        /*        User::class.members.forEach { member ->
                    val mioColumn = member.annotations.find { it.annotationClass == MioColumn::class }
                    if (member.name.contains("component")
                        || member.name == "copy"
                        || member.name == "equals"
                        || member.name == "hashCode"
                        || member.name == "toString"
                    ) {
                        return@forEach
                    }

                    mioColumn?.let {
                        val index = it.getProperty("index") ?: return
                        columnList.add(
                            Temp(
                                member.name,
                                index as Int,
                                it.getProperty("title") as String,
                                it.getProperty("type") as CellType,
                                it.getProperty("width") as Int,
                            )
                        )
                    }
                }
                if (!columnList.isNullOrEmpty()) {
                    columnList.sortBy { it.index }
                    columnList.forEach {
                        Log.d(TAG, "initView column list: $it")
                    }
                }

                data[0]::class.members.find { it.name == "id" }?.let {
                    Log.d(
                        TAG,
                        "initView: info: ${it.getAnnotationValue(MyPropertyAnnotation::class, "info")}"
                    )

        //            it.annotations.forEach {
        //                Log.d(TAG, "initView: ${it.annotationClass}")
        //            }
                    // 获取注解的值
                    val annotation =
                        it.annotations.find { it.annotationClass == MyPropertyAnnotation::class }
                    Log.d(TAG, "initView: ${annotation?.getProperty("info")}")
                }

                val members = data[0].getMembers() // 标题列*/

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

    private fun bindInnerRv(rv: RecyclerView, user: User, members: MutableList<String>) {
        rv.apply {
            adapter = object : BaseRvAdapter<String, ItemCellBinding>(
                members,
                R.layout.item_cell
            ) {
                override fun bindData(binding: ItemCellBinding, bean: String?, position: Int) {
//                    binding.tv.text = user.getProperty(bean!!).toString()
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

    fun setData(data: List<User>) {
        rvAdapter.setData(data)
    }

    data class Temp(
        val member: String,
        val index: Int,
        val title: String,
        val type: CellType,
        val width: Int,
    )
}