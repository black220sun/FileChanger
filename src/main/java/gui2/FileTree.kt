package gui2

import settings.Settings
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File

import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
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
        maximumSize = Dimension(400, 600)
    }

    fun getSelected(): List<File> {
        return tree.selectionPaths?.map { (it.lastPathComponent as DefaultMutableTreeNode).userObject as File } ?: ArrayList()
    }

    private class FileTreeModel(dir: File): DefaultTreeModel(DefaultMutableTreeNode(dir)) {
        override fun isLeaf(node: Any): Boolean {
            try {
                val file = (node as DefaultMutableTreeNode).userObject as File
                if (!file.isDirectory)
                    return false
                val files = file.listFiles() ?: return true
                return files.all { !it.isDirectory }
            } catch (e: Exception) {
                println((node as DefaultMutableTreeNode).userObject)
                return true
            }
        }

        override fun getChildCount(node: Any): Int {
            val file = (node as DefaultMutableTreeNode).userObject as File
            return when {
                !file.isDirectory -> 0
                Settings.showHidden() -> file.listFiles().count { it.isDirectory }
                else -> file.listFiles().count { !it.isHidden && it.isDirectory }
            }

        }

        override fun getChild(node: Any, index: Int): Any {
            val parent = (node as DefaultMutableTreeNode).userObject as File
            return DefaultMutableTreeNode(
                    if (Settings.showHidden())
                        parent.listFiles { file -> file.isDirectory }[index]
                    else
                        parent.listFiles { file -> file.isDirectory && !file.isHidden }[index])
        }
    }
}
