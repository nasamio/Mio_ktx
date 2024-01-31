package com.mio.pathplanning

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mio.base.Tag.TAG
import com.mio.base.toast
import com.mio.pathplanning.bean.Coord
import com.mio.pathplanning.bean.MapInfo
import com.mio.pathplanning.bean.Node
import com.mio.pathplanning.ui.PathView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch(Dispatchers.IO) {
            test()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun test() {
        Log.d(TAG, "test: ")
        // 源地图数据
//        val maps = arrayOf(
//            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
//            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
//            intArrayOf(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0),
//            intArrayOf(0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0),
//            intArrayOf(0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0),
//            intArrayOf(0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0),
//            intArrayOf(0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0)
//        )
//        val startPos = Coord(1, 1)
//        val endPos = Coord(4, 5)

        // 随机生成起点和终点的坐标

        val m = 120
        val n = 70
        val startX = Random.nextInt(maxOf(minOf(n, m) - 1, 0))
        val startY = Random.nextInt(maxOf(minOf(n, m) - 1, 0))
        val endX = Random.nextInt(maxOf(minOf(n, m) - 1, 0))
        val endY = Random.nextInt(maxOf(minOf(n, m) - 1, 0))

        val startPos = Coord(startX, startY)
        val endPos = Coord(endX, endY)
        val maps = generateMapData(n, m, startPos, endPos)
        val updateUi = true

        Log.d(TAG, "test: 网格规模: ${maps.size} * ${maps[0].size}")
        Log.d(TAG, "test: startPos: $startPos")
        Log.d(TAG, "test: endPos: $endPos")


        if (updateUi){
            withContext(Dispatchers.Main) {
                findViewById<PathView>(R.id.pathView).setData2(maps.clone(), startPos, endPos)
            }
        }
        val info = MapInfo(maps, maps[0].size, maps.size, Node(startPos), Node(endPos))
        val startTime = System.currentTimeMillis()
        AStar().start(info) {
            val dt = System.currentTimeMillis() - startTime
            Log.d(TAG, "test: 耗时 $dt ms")

            GlobalScope.launch(Dispatchers.Main) {
                this@MainActivity.toast("耗时 $dt ms")
                if (updateUi) {
                    delay(1000)
                    findViewById<PathView>(R.id.pathView).apply {
                        lineList.clear()
                        lineList.addAll(it)
                        setData2(maps, startPos, endPos)
                    }
                }
            }
        }


    }

    fun printMap(maps: Array<IntArray>) {
        // 日志输出二维数据
        for (i in maps.indices) {
            var line = ""
            for (j in maps[i].indices) {
                line += maps[i][j].toString()
            }
            Log.d(TAG, "printMap: $line")
        }

        Log.d(TAG, "printMap: ---------------")
    }

    // 生成一个n*m的地图数据，用于测试A*算法寻路
    fun generateMapData(n: Int, m: Int, start: Coord, end: Coord): Array<IntArray> {
        // 创建一个n*m的二维数组，初始值都为0
        val mapData = Array(n) { IntArray(m) { 0 } }
        // 生成一个从起点到终点的随机路径，用一个生成序列的函数
        val path = generateSequence(Pair(start.x, start.y)) { (x, y) ->
            // 如果到达终点，返回null，结束序列
            if (x == end.x && y == end.y) null
            else {
                // 否则，随机选择一个方向，向前移动一步，返回新的坐标
                val direction = Random.nextInt(4)
                when (direction) {
                    0 -> Pair(x + 1, y).takeIf { x + 1 < n } // 向右
                    1 -> Pair(x - 1, y).takeIf { x - 1 >= 0 } // 向左
                    2 -> Pair(x, y + 1).takeIf { y + 1 < m } // 向下
                    3 -> Pair(x, y - 1).takeIf { y - 1 >= 0 } // 向上
                    else -> null // 不会发生
                }
            }
        }
        // 遍历路径上的每个坐标，将对应的数组元素设为0，表示可通行
        for ((x, y) in path) {
            mapData[x][y] = 0
        }
        // 随机生成一些障碍物，将对应的数组元素设为1，表示不可通行
        // 这里可以调整障碍物的数量和分布
        val obstacleCount = Random.nextInt(n * m / 10, n * m / 5) // 障碍物的数量在总格子数的10%到20%之间
        repeat(obstacleCount) {
            // 随机生成一个坐标，如果不是起点或终点，就设为障碍物
            val x = Random.nextInt(n)
            val y = Random.nextInt(m)
            if ((x != start.x || y != start.y) && (x != end.x || y != end.y)) {
                mapData[x][y] = 1
            }
        }
        // 返回生成的地图数据
        return mapData
    }

}