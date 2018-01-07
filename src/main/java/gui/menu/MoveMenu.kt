package gui.menu

import gui.MainController
import gui.TableModel
import settings.Settings
import javax.swing.*

class MoveMenu : JMenu(Settings.getLang("Move")) {
    private val changer = TableModel.changer
    init {
        setMnemonic('M')

        val force = JCheckBoxMenuItem(Settings.getLang("Force move"), false)
        force.setMnemonic('F')

        val toDir = JMenuItem(Settings.getLang("To directory"))
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

        val byName = JMenuItem(Settings.getLang("By name"))
        byName.setMnemonic('n')
        byName.addActionListener { getLines(force = force.state) }
        add(byName)

        val dirName = JMenuItem(Settings.getLang("To directory by name"))
        dirName.setMnemonic('T')
        dirName.addActionListener {
            val chooser = JFileChooser()
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (chooser.showOpenDialog(parent.parent) != JFileChooser.APPROVE_OPTION)
                return@addActionListener
            getLines(force = force.state, path = chooser.selectedFile.absolutePath)
        }
        add(dirName)

        add(force)
    }

    private fun getLines(force: Boolean, path: String = "") {
        val from = JOptionPane.showInputDialog(parent.parent, Settings.getLang("After characters:"),
                Settings.getLang("Get directory name"), JOptionPane.QUESTION_MESSAGE) ?: return
        val to = JOptionPane.showInputDialog(parent.parent, Settings.getLang("Until characters:"),
                Settings.getLang("Get directory name"), JOptionPane.QUESTION_MESSAGE) ?: return
        val files = changer.getFiles()
        val new =
                if (path.isEmpty())
                    changer.moveByName(force = force, from = from, to = to)
                else
                    changer.moveByName(force = force, from = from, to = to, path = path)
        MainController.results(arrayListOf(files, new), force)
    }
}