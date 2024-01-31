package com.mio.pathplanning

import android.util.Log
import com.mio.base.Tag.TAG
import com.mio.pathplanning.bean.Coord
import com.mio.pathplanning.bean.MapInfo
import com.mio.pathplanning.bean.Node
import java.util.PriorityQueue
import java.util.Queue


class AStar {
    val BAR = 1 // 障碍值
    val PATH = 2 // 路径
    val DIRECT_VALUE = 10 // 横竖移动代价
    val OBLIQUE_VALUE = 14 // 斜移动代价

    val openList: Queue<Node> = PriorityQueue<Node>() // 优先队列(升序)
    val closeList: MutableList<Node> = ArrayList<Node>()
    var callback: ((MutableList<Coord>) -> Unit)? = null

    fun start(mapInfo: MapInfo?, callback: ((MutableList<Coord>) -> Unit)? = null) {
        this.callback = callback
        mapInfo?.let {
            // 清除之前的
            openList.clear()
            closeList.clear()
            // 开始搜索
            openList.add(mapInfo.start)
            moveNodes(mapInfo)
            // 输出路径
            openList.forEach {
                Log.d(TAG, "start: openList: ${it.coord}")
            }
        }
    }

    fun moveNodes(mapInfo: MapInfo) {
        while (!openList.isEmpty()) {
            val current = openList.poll()
            closeList.add(current)
            addNeighborNodeInOpen(mapInfo, current)
            if (isCoordInClose(mapInfo.end.coord)) {
                drawPath(mapInfo.maps, mapInfo.end)
                break
            }
        }
    }

    private fun drawPath(maps: Array<IntArray>?, end: Node) {
        var end: Node? = end
        if (end == null || maps == null) return
        System.out.println("总代价：" + end.G)
        val res = mutableListOf<Coord>()
        while (end != null) {
            val (x, y) = end.coord

            res.add(end.coord)

            maps[y][x] = PATH
            end = end.parent
        }

        res.reverse()
        callback?.invoke(res)
    }

    /**
     * 添加所有邻结点到open表
     */
    private fun addNeighborNodeInOpen(mapInfo: MapInfo, current: Node) {
        val x = current.coord.x
        val y = current.coord.y
        // 左
        addNeighborNodeInOpen(mapInfo, current, x - 1, y, DIRECT_VALUE)
        // 上
        addNeighborNodeInOpen(mapInfo, current, x, y - 1, DIRECT_VALUE)
        // 右
        addNeighborNodeInOpen(mapInfo, current, x + 1, y, DIRECT_VALUE)
        // 下
        addNeighborNodeInOpen(mapInfo, current, x, y + 1, DIRECT_VALUE)
        // 左上
        addNeighborNodeInOpen(mapInfo, current, x - 1, y - 1, OBLIQUE_VALUE)
        // 右上
        addNeighborNodeInOpen(mapInfo, current, x + 1, y - 1, OBLIQUE_VALUE)
        // 右下
        addNeighborNodeInOpen(mapInfo, current, x + 1, y + 1, OBLIQUE_VALUE)
        // 左下
        addNeighborNodeInOpen(mapInfo, current, x - 1, y + 1, OBLIQUE_VALUE)
    }

    private fun addNeighborNodeInOpen(mapInfo: MapInfo, current: Node, x: Int, y: Int, value: Int) {
        if (canAddNodeToOpen(mapInfo, x, y)) {
            val end = mapInfo.end
            val coord = Coord(x, y)
            val G = current.G + value // 计算邻结点的G值
            var child: Node? = findNodeInOpen(coord)
            if (child == null) {
                val H: Int = calcH(end.coord, coord) // 计算H值
                if (isEndNode(end.coord, coord)) {
                    child = end
                    child.parent = current
                    child.G = G
                    child.H = H
                } else {
                    child = Node(coord, current, G, H)
                }
                openList.add(child)
            } else if (child.G > G) {
                child.G = G
                child.parent = current
                openList.add(child)
            }
        }
    }

    /**
     * 从Open列表中查找结点
     */
    private fun findNodeInOpen(coord: Coord?): Node? {
        if (coord == null || openList.isEmpty()) return null
        for (node in openList) {
            if (node.coord.equals(coord)) {
                return node
            }
        }
        return null
    }

    /**
     * 计算H的估值：“曼哈顿”法，坐标分别取差值相加
     */
    private fun calcH(end: Coord, coord: Coord): Int {
        return (Math.abs(end.x - coord.x) + Math.abs(end.y - coord.y)) * DIRECT_VALUE
    }

    /**
     * 判断结点是否是最终结点
     */
    private fun isEndNode(end: Coord, coord: Coord?): Boolean {
        return coord != null && end.equals(coord)
    }

    /**
     * 判断结点能否放入Open列表
     */
    private fun canAddNodeToOpen(mapInfo: MapInfo, x: Int, y: Int): Boolean {
        // 是否在地图中
        if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.height) return false
        // 判断是否是不可通过的结点
        if (mapInfo.maps[y][x] == BAR) return false
        // 判断结点是否存在close表
        return if (isCoordInClose(x, y)) false else true
    }

    /**
     * 判断坐标是否在close表中
     */
    private fun isCoordInClose(coord: Coord?): Boolean {
        return coord != null && isCoordInClose(coord.x, coord.y)
    }

    /**
     * 判断坐标是否在close表中
     */
    private fun isCoordInClose(x: Int, y: Int): Boolean {
        if (closeList.isEmpty()) return false
        for ((coord) in closeList) {
            if (coord.x === x && coord.y === y) {
                return true
            }
        }
        return false
    }
}