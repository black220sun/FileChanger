package gui.menu

import gui.MainController
import gui.TableModel
import gui.util.LMenu
import gui.util.LMenuItem
import settings.Settings
import javax.swing.*

class MoveMenu : LMenu("Move") {
    private val changer = TableModel.changer
    init {
        setMnemonic('M')

        val toDir = LMenuItem("To directory")
        toDir.setMnemonic('d')
        toDir.addActionListener {
            val chooser = JFileChooser()
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (chooser.showOpenDialog(parent.parent) != JFileChooser.APPROVE_OPTION)
                return@addActionListener
            val files = changer.getFiles()
            val force = Settings.getForce("forceMove")
            val new = changer.move(force = force, dir = chooser.selectedFile)
            MainController.results(arrayListOf(files, new), force)
        }
        add(toDir)

        val byName = LMenuItem("By name")
        byName.setMnemonic('n')
        byName.addActionListener { getLines(force = Settings.getForce("forceMove")) }
        add(byName)

        val dirName = LMenuItem("To directory by name")
        dirName.setMnemonic('T')
        dirName.addActionListener {
            val chooser = JFileChooser()
            chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            if (chooser.showOpenDialog(parent.parent) != JFileChooser.APPROVE_OPTION)
                return@addActionListener
            getLines(force = Settings.getForce("forceMove"), path = chooser.selectedFile.absolutePath)
        }
        add(dirName)
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