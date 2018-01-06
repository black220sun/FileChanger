package gui.menu

import gui.TableModel
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import java.io.File

class MenuBar : JMenuBar() {
    init {
        add(buildFilesMenu())
    }

    private fun buildFilesMenu(): JMenu {
        val menu = JMenu("Files")
        menu.setMnemonic('F')
        val hidden = JCheckBoxMenuItem("Show hidden files", false)
        hidden.setMnemonic('h')
        val open = JMenuItem("Open")
        open.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
        open.setMnemonic('O')
        open.addActionListener {
            val fileChooser =  JFileChooser()
            fileChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
            fileChooser.isMultiSelectionEnabled = true
            fileChooser.isFileHidingEnabled = !hidden.state
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { TableModel.add(it) }
        }
        menu.add(open)
        val dir = JMenuItem("Add from directory")
        dir.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK)
        dir.setMnemonic('A')
        dir.addActionListener {
            val fileChooser =  JFileChooser()
            fileChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
            fileChooser.isMultiSelectionEnabled = true
            fileChooser.isFileHidingEnabled = !hidden.state
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { addFiles(it) }
        }
        menu.add(dir)
        menu.add(hidden)
        val clear = JMenuItem("Clear")
        clear.setMnemonic('C')
        clear.addActionListener { TableModel.clear() }
        menu.add(clear)
        return menu
    }

    private fun addFiles(file: File) {
        if (file.isFile)
            TableModel.add(file)
        else if (file.isDirectory)
            file.listFiles().forEach { addFiles(it) }
    }
}