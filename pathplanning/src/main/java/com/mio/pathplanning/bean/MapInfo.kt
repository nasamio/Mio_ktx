package com.mio.pathplanning.bean

data class MapInfo(
    // public int[][] maps; // 二维数组的地图
    var maps: Array<IntArray>,
    var width: Int,
    var height: Int,
    var start: Node,
    var end: Node,
)
