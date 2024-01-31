package com.mio.pathplanning.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.mio.base.Tag.TAG
import com.mio.pathplanning.bean.Coord

class PathView(context: Context, attr: AttributeSet) : View(context, attr) {
    var data: Array<IntArray>? = null

    val itemWidth = 10
    val itemHeight = 10
    val offsetX = 10
    val offsetY = 10

    // 网格
    val gridPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }

    // 障碍
    val barPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        strokeWidth = 1f
        isAntiAlias = true
    }

    // 非障碍
    val normalPaint = Paint().apply {
        color = Color.parseColor("#FFC0CB")
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    //  起点
    val startPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        strokeWidth = 1f
        isAntiAlias = true
    }

    // 终点
    val endPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        strokeWidth = 1f
        isAntiAlias = true
    }

    // 路线列表
    val lineList = mutableListOf<Coord>()

    override fun onDraw(canvas: Canvas) {
        data?.let {
            val i = it.size
            val j = it[0].size
            Log.d(TAG, "onDraw: i: ${i}, j: ${j}")
            // 绘制网格
            for (x in 0 until i + 1) {
                canvas.drawLine(
                    offsetX.toFloat(),
                    (offsetY + x * itemHeight).toFloat(),
                    (offsetX + j * itemWidth).toFloat(),
                    (offsetY + x * itemHeight).toFloat(),
                    gridPaint
                )
            }
            for (y in 0 until j + 1) {
                canvas.drawLine(
                    (offsetX + y * itemWidth).toFloat(),
                    offsetY.toFloat(),
                    (offsetX + y * itemWidth).toFloat(),
                    (offsetY + i * itemHeight).toFloat(),
                    gridPaint
                )
            }
            // 绘制每一格中的东西 起点 终点 障碍
            for (y in 0 until i) {
                for (x in 0 until j) {
                    val gx = x
                    val gy = y
                    val v = it[gy][gx]
                    Log.d(TAG, "onDraw: (x: ${x}, y: ${y}): ${it[y][x]}")
                    when (v) {
                        0 -> {

                        }

                        1 -> {
                            canvas.drawRect(
                                (offsetX + x * itemWidth).toFloat(),
                                (offsetY + y * itemHeight).toFloat(),
                                (offsetX + x * itemWidth + itemWidth).toFloat(),
                                (offsetY + y * itemHeight + itemHeight).toFloat(),
                                barPaint
                            )
                        }

                        3 -> {
                            canvas.drawRect(
                                (offsetX + x * itemWidth).toFloat(),
                                (offsetY + y * itemHeight).toFloat(),
                                (offsetX + x * itemWidth + itemWidth).toFloat(),
                                (offsetY + y * itemHeight + itemHeight).toFloat(),
                                startPaint
                            )
                        }

                        4 -> {
                            canvas.drawRect(
                                (offsetX + x * itemWidth).toFloat(),
                                (offsetY + y * itemHeight).toFloat(),
                                (offsetX + x * itemWidth + itemWidth).toFloat(),
                                (offsetY + y * itemHeight + itemHeight).toFloat(),
                                endPaint
                            )
                        }
                    }
                }
            }

            Log.d(TAG, "onDraw: lineList.size = ${lineList.size}")
            // 绘制路线
            if (lineList.size > 0) {
                Log.d(TAG, "onDraw: lineList.size = ${lineList.size}")
                canvas.drawPath(
                    pathList2Path(lineList),
                    normalPaint
                )
            }
        }
    }

    private fun pathList2Path(lineList: MutableList<Coord>): Path {
        val path = Path()
        path.moveTo(
            (offsetX + lineList[0].x * itemWidth + itemWidth / 2).toFloat(),
            (offsetY + lineList[0].y * itemHeight + itemHeight / 2).toFloat()
        )
        for (i in 1 until lineList.size) {
            path.lineTo(
                (offsetX + lineList[i].x * itemWidth + itemWidth / 2).toFloat(),
                (offsetY + lineList[i].y * itemHeight + itemHeight / 2).toFloat()
            )
        }
        return path
    }

    fun setData2(maps: Array<IntArray>, start: Coord, end: Coord) {
        data = maps


        // 遍历maps 如果有2 就加入到lineList中
//        for (i in maps.indices) {
//            for (j in maps[i].indices) {
//                if (maps[i][j] == 2) {
//                    lineList.add(Coord(i, j))
//                }
//            }
//        }

        data?.let {
//            it[start.x][start.y] = 3
//            it[end.x][end.y] = 4
            val sx = start.x
            val sy = start.y
            val ex = end.x
            val ey = end.y

            it[sy][sx] = 3
            it[ey][ex] = 4
        }

        invalidate()
    }
}