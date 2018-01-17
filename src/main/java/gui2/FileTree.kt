package gui2

import settings.Settings
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.io.File
import javax.swing.*

import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer
import javax.swing.tree.DefaultTreeModel

class FileTree(dir: File) : JPanel() {
    private val tree = JTree(FileTreeModel(dir))

    init {
        layout = BorderLayout()
        val scroll = JScrollPane()
        scroll.viewport.add(tree)
        add(BorderLayout.CENTER, scroll)

        preferredSize = Dimension(300, 600)
        minimumSize = Dimension(200, 0)
        maximumSize = Dimension(400, 1000)

        tree.scrollsOnExpand = true
        tree.cellRenderer = Renderer()

        tree.addTreeSelectionListener {
            val path = it.newLeadSelectionPath ?: return@addTreeSelectionListener
            val file = (path.lastPathComponent as DefaultMutableTreeNode).userObject as File
            MainView.show(file)
        }
    }

    fun getSelected(): List<File> {
        return tree.selectionPaths?.map { (it.lastPathComponent as DefaultMutableTreeNode).userObject as File } ?: ArrayList()
    }

    fun setRoot(file: File) {
        tree.model = FileTreeModel(file)
    }

    fun update() = (tree.model as FileTreeModel).reload()

    private class FileTreeModel(dir: File) : DefaultTreeModel(DefaultMutableTreeNode(dir)) {
        override fun isLeaf(node: Any): Boolean {
            val file = (node as DefaultMutableTreeNode).userObject as File
            if (!file.isDirectory)
                return false
            val files = file.listFiles() ?: return true
            return files.all { !it.isDirectory }
        }

        override fun getChildCount(node: Any): Int {
            val file = (node as DefaultMutableTreeNode).userObject as File
            return when {
                !file.isDirectory -> 0
                Settings.showHidden() -> file.listFiles().count { it.isDirectory }
                else -> file.listFiles().count { it.isDirectory && !it.isHidden }
            }

        }

        override fun getChild(node: Any, index: Int): Any {
            val parent = (node as DefaultMutableTreeNode).userObject as File
            return DefaultMutableTreeNode(
                    if (Settings.showHidden())
                        parent.listFiles { file -> file.isDirectory }.sorted()[index]
                    else
                        parent.listFiles { file -> file.isDirectory && !file.isHidden }.sorted()[index])
        }
    }

    private class Renderer : DefaultTreeCellRenderer() {
        init {
            leafIcon = closedIcon
        }
        override fun getTreeCellRendererComponent(p0: JTree?, p1: Any, p2: Boolean, p3: Boolean, p4: Boolean, p5: Int, p6: Boolean): Component {
            val file = (p1 as DefaultMutableTreeNode).userObject as File
            return super.getTreeCellRendererComponent(p0, file.name, p2, p3, p4, p5, p6)
        }
    }
}
