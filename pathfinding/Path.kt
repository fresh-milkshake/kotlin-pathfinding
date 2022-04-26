package pathfinding

// pathfinding.Path class that contains the chain of nodes that form the path
class Path(var nodes: List<Node> = listOf()) {
    val start: Node
        get() = nodes.last()

    val end: Node
        get() = nodes.first()

    val pathNodes: List<Node>
        get() = if (nodes.size > 2) nodes.subList(1, nodes.size - 1) else listOf()

    fun add(node: Node) {
        nodes = nodes.plus(node)
    }

    fun disband() {
        nodes = listOf()
    }

    fun disbandPath(vararg new: Node) {
        nodes = new.toList()
    }

    fun isEmpty(): Boolean {
        return nodes.isEmpty()
    }

    override fun toString(): String {
        return "Size of path: ${nodes.size}\npathfinding.Path: ${nodes.isEmpty()}"
    }
}