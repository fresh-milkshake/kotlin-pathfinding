package pathfinding

import kotlin.math.abs
import kotlin.math.min


fun manhattanDistance(a: Node, b: Node): Int {
    return abs(a.row - b.row) + abs(a.col - b.col)
}

fun diagonalDistance(a: Node, b: Node): Int {
    val d = 1   // cost of moving in a straight line
    val d2 = 2  // cost of moving in a diagonal line

    val dx = abs(a.col - b.col)
    val dy = abs(a.row - b.row)
    return d * (dx + dy) + (d2 - 2 * 1) * min(dx, dy)
}