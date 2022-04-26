package pathfinding

import Point

// pathfinding.Node class that contains the position of the node and the previous node
class Node(val row: Int, val col: Int) {
    val position: Pair<Int, Int> = Pair(col, row)

    // gScore is the distance from the start node to the current node
    var gScore: Int = Int.MAX_VALUE
    // hScore is the distance from the start node to the current node
    // plus the distance from the current node to the end node
    var fScore: Int = Int.MAX_VALUE
    var isSolid: Boolean = false
    var parent: Node? = null
    val point: Point = Point(row, col)

}