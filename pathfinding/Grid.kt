package pathfinding

import Point
import java.util.Stack
import java.util.PriorityQueue
import kotlin.random.Random


// grid is a 2D array of nodes
// width and height are the dimensions of the grid
// nodes - optional, if provided, the nodes will be used instead of creating new ones
class Grid(val width: Int, val height: Int, private var nodes: Array<Array<Node?>>? = null) {
    // if nodes are provided, they will be used instead of creating new ones
    init {
        if (nodes == null) {
            // generate nodes and set their coordinates to their position in the array
            nodes = Array(height) { row ->
                Array(width) { col ->
                    Node(row, col)
                }
            }
        }
    }
    // get node at x, y
    fun getNode(row: Int, col: Int): Node {
        return nodes!![row][col]!!
    }

    // get node at a given point
    fun getNode(point: Point): Node {
        return nodes!![point.row][point.col]!!
    }

    // find free neighbors of a node and return random one
    fun findFreeNodeNear(point: Point): Node {
        var node = getNode(point)
        while (node.isSolid) {
            val neighbors = getNeighbors(node)
            for (n in neighbors) {
                if (!n.isSolid) {
                    return n
                }
            }
            node = neighbors.random()
        }
        return node
    }

    // find free neighbors of a node and return random one
    fun findFreeNodeNear(row: Int, col: Int): Node {
        var node = getNode(row, col)
        while (node.isSolid) {
            val neighbors = getNeighbors(node)
            for (n in neighbors) {
                if (!n.isSolid) {
                    return n
                }
            }
            node = neighbors.random()
        }
        return node
    }

    // set node at x, y
    fun setNode(row: Int, col: Int, node: Node) {
        nodes!![col][row] = node
    }

    // set node at a given point
    fun setNode(point: Point, node: Node) {
        nodes!![point.row][point.col] = node
    }

    // generate random solid obstacles
    fun generateObstacles(obstaclePercent: Int) {
        for (row in 0 until height) {
            for (col in 0 until width) {
                if (Random.nextInt(100) < obstaclePercent) {
                    // create group of solid nodes
                    val node = getNode(row, col)
                    val group = getNeighbors(node) { true }
                    group.forEach { it.isSolid = true }
                    node.isSolid = true
                }
            }
        }
    }

    // print the grid
    fun show(highlight: List<Node> = listOf(), additional: MutableMap<Node, String> = mutableMapOf()) {
        for (row in 0 until height) {
            for (col in 0 until width) {
                val node = getNode(row, col)
                if (node.isSolid) {
                    print(" #")
                } else {
                    if (additional.containsKey(node)) {
                        print(" ${additional[node]}")
                    } else if (highlight.contains(node)) {
                        print(" •")
                    } else {
                        print("  ")
                    }
                }
            }
            println()
        }
    }

    // validate given coordinates
    private fun validateCoordinates(row: Int, col: Int): Boolean {
        return row in 0 until height && col in 0 until width
    }

    // get all nodes around a given node
    // and return them as a list of neighbours
    // method receives a node and predicate that determines if a node satisfies the condition
    private fun getNeighbors(node: Node, predicate: (Node) -> Boolean = { true }): List<Node> {
        val neighbors = ArrayList<Node>()
        for (row in -1..1) {
            for (col in -1..1) {
                if (row == 0 && col == 0) continue
                if (validateCoordinates(node.row + row, node.col + col)
                    && predicate(getNode(node.row + row, node.col + col))) {
                    neighbors.add(getNode(node.row + row, node.col + col))
                }
            }
        }
        return neighbors
    }

    // function of reconstructing the path
    private fun reconstructPath(cameFrom: MutableMap<Node, Node>, current: Node): Path {
        var currentNode = current
        val totalPath = Path()
        totalPath.add(currentNode)
        while (cameFrom.containsKey(currentNode)) {
            currentNode = cameFrom[currentNode]!!
            totalPath.add(currentNode)
        }
        return totalPath
    }

    // A* algorithm implementation for finding a path in a grid
    // start - the starting node
    // end - the end node
    // heuristic - the heuristic function to use
    // returns a path object
    fun findPath(
        start: Node,
        end: Node,
        heuristic: (Node, Node) -> Int,
        neighboursPredicate: (Node) -> Boolean = { n -> !n.isSolid }
    ): Path {
        start.fScore = heuristic(start, end)
        start.gScore = 0

        // set of visited nodes
        val openSet = mutableListOf(start)

        // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from start
        // to n currently known.
        // now cameFrom is an empty map of nodes
        val cameFrom = mutableMapOf<Node, Node>()

        // For node n, fScore[n] := gScore[n] + h(n).
        // fScore is a PriorityQueue, sorted by the fScore of the nodes
        // this will give us the lowest fScore first and
        // the speed of execution becomes O(Log(N)) instead of O(N)
        val fScore = PriorityQueue<Node>(compareBy { it.fScore })
        fScore.add(start)

        // while openSet is not empty
        while (openSet.isNotEmpty()) {

            // current := the node in openSet having the lowest fScore[] value
            val current: Node = fScore.poll()!!

            // if current = end
            if (current.position == end.position) {
                // return reconstruct_path(cameFrom, end)
                println("Path found!")
                return reconstructPath(cameFrom, current)
            }

            // remove current from openSet
            openSet.remove(current)

            // print grid and fill openSet with X's
//            show(highlight = openSet, additional = mutableMapOf(start to "S", end to "E"))

            // for each neighbor of current
            val neighbors = getNeighbors(current, neighboursPredicate)
            for (neighbor in neighbors) {
                // tentative_gScore := gScore[current] + dist_between(current, neighbor)
                val tentativeGScore = current.gScore + 1
                if (tentativeGScore < neighbor.gScore) {
                    // cameFrom[neighbor] := current
                    cameFrom[neighbor] = current
                    neighbor.parent = current
                    // gScore[neighbor] := tentative_gScore
                    neighbor.gScore = tentativeGScore
                    // fScore[neighbor] := gScore[neighbor] + h(neighbor)
                    neighbor.fScore = neighbor.gScore + heuristic(neighbor, end)
                    fScore.add(neighbor)
                    // if neighbor is not in openSet
                    if (!openSet.contains(neighbor)) {
                        // add neighbor to openSet
                        openSet.add(neighbor)
                    }
                }
            }
        }
        println("No path found")
        return reconstructPath(cameFrom, end)
    }
}
