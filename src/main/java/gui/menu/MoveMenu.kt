package gui.menu

import gui.MainController
import gui.TableModel
import javax.swing.*

class MoveMenu : JMenu("Move") {
    private val changer = TableModel.changer
    init {
        setMnemonic('M')

        val force = JCheckBoxMenuItem("Force move", false)
        force.setMnemonic('F')

        val toDir = JMenuItem("To directory")
        toDir.setMnemonic('d')
        toDir.addActionListener {
            val chooser = JFileChooser()
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (chooser.showOpenDialog(parent.parent) != JFileChooser.APPROVE_OPTION)
                return@addActionListener
            val files = changer.getFiles()
            val new = changer.move(force = force.state, dir = chooser.selectedFile)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(toDir)

        val byName = JMenuItem("By name")
        byName.setMnemonic('n')
        byName.addActionListener {
            val from = JOptionPane.showInputDialog(parent.parent, "After characters:", "Get directory name", JOptionPane.QUESTION_MESSAGE) ?: return@addActionListener
            val to = JOptionPane.showInputDialog(parent.parent, "Until characters:", "Get directory name", JOptionPane.QUESTION_MESSAGE) ?: return@addActionListener
            val files = changer.getFiles()
            val new = changer.moveByName(force = force.state, from = from, to = to)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(byName)

        val dirName = JMenuItem("To directory by name")
        dirName.setMnemonic('T')
        dirName.addActionListener {
            val chooser = JFileChooser()
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (chooser.showOpenDialog(parent.parent) != JFileChooser.APPROVE_OPTION)
                return@addActionListener
            val from = JOptionPane.showInputDialog(parent.parent, "After characters:", "Get directory name", JOptionPane.QUESTION_MESSAGE) ?: return@addActionListener
            val to = JOptionPane.showInputDialog(parent.parent, "Until characters:", "Get directory name", JOptionPane.QUESTION_MESSAGE) ?: return@addActionListener
            val files = changer.getFiles()
            val new = changer.moveByName(force = force.state, from = from, to = to,
                    path = chooser.selectedFile.absolutePath)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(dirName)

        add(force)
    }
}