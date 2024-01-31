package com.mio.pathplanning.bean

/**
 * 节点
 */
data class Node(
    var coord: Coord,
    var parent: Node? = null,
    var G: Int = 0,
    var H: Int = 0,
) : Comparable<Node> {
    override fun compareTo(o: Node): Int {
        if (o == null) return -1
        if (G + H > o.G + o.H)
            return 1;
        else if (G + H < o.G + o.H) return -1;
        return 0;
    }

}

