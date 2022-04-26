package pathfinding

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Panel
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JFrame
import kotlin.math.abs
import kotlin.math.min

val CELL_SIZE = 10

// class that visualizes the results of the pathfinding algorithm
// draws the grid, the start and end nodes, the path, and the nodes that were searched
class GUI(private val grid: Grid, val path: Path) : JFrame() {
    private val panel = GridPanel(grid, path)

    init {
        title = "Pathfinding"
        defaultCloseOperation = EXIT_ON_CLOSE
        // set size
        size = Dimension(grid.width * CELL_SIZE, grid.height * CELL_SIZE)
        add(panel)
        pack()
        isVisible = true
    }

    fun draw() {
        panel.repaint()
    }
}

class GridPanel(val grid: Grid, var path: Path) : Panel() {

    init {
        // add and initialize mouse motion listener
        addMouseListener(object : MouseMotionListener, MouseListener {
            override fun mouseDragged(e: MouseEvent?) {}

            override fun mouseMoved(e: MouseEvent?) {}

            override fun mouseClicked(e: MouseEvent?) {
                // get the node that the mouse is currently hovering over

                val node = grid.getNode(e!!.y / CELL_SIZE, e.x / CELL_SIZE)

                if (e.button == 1) {
                    val start = grid.findFreeNodeNear(node.point)
                    path.disbandPath(path.end, start)
                } else if (e.button == 3) {
                    val end = grid.findFreeNodeNear(node.point)
                    path.disbandPath(end, path.start)
                }

                path = grid.findPath(path.start, path.end, ::diagonalDistance)

                // redraw the grid
                repaint()
            }

            override fun mousePressed(e: MouseEvent?) {}

            override fun mouseReleased(e: MouseEvent?) {}

            override fun mouseEntered(e: MouseEvent?) {}

            override fun mouseExited(e: MouseEvent?) {}
        })

    }
    // set size
    override fun getPreferredSize() = Dimension(grid.width * CELL_SIZE, grid.height * CELL_SIZE)

    override fun paint(g: Graphics) {
        super.paint(g)
        drawGrid(g)
    }

    private fun drawGrid(g: Graphics) {
        for (row in 0 until grid.height) {
            for (col in 0 until grid.width) {
                val node = grid.getNode(row, col)
                when (node) {
                    path.start -> {
                        g.color = Color.GREEN
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE)
                    }
                    path.end -> {
                        g.color = Color.RED
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE)
                    }
                    in path.pathNodes -> {
                        g.color = Color.BLUE
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE)
                    }
                }

                if (node.isSolid) {
                    g.color = Color.BLACK
                    g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE)
                }
            }
        }
    }
}


