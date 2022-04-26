package pathfinding

import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


@OptIn(ExperimentalTime::class)
fun main() {
    val from: Node
    val goal: Node

    // generate a grid and test the A* algorithm
    val grid = Grid(20, 10)
        .also {
            it.generateObstacles(3)
            from = it.findFreeNodeNear(0, 0)
            goal = it.findFreeNodeNear(it.height-1, it.width-1)
        }
    println("from ${from.position} to ${goal.position}")
    val (path, _) = measureTimedValue {
        grid.findPath(from, goal, ::diagonalDistance)
    }
    GUI(grid, path)
}