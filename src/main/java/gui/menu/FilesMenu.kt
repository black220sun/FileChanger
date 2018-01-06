package gui.menu

import gui.MainController
import gui.TableModel
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.*

class FilesMenu: JMenu("Files") {
    init {
        setMnemonic('F')
        val hidden = JCheckBoxMenuItem("Show hidden files", false)
        hidden.setMnemonic('h')
        val open = JMenuItem("Open")
        open.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
        open.setMnemonic('O')
        open.addActionListener {
            val fileChooser =  buildFileChooser(hidden.state)
            if (fileChooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { TableModel.add(it) }
        }
        add(open)
        val dir = JMenuItem("Add from directory")
        dir.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK)
        dir.setMnemonic('A')
        dir.addActionListener {
            val fileChooser =  buildFileChooser(hidden.state)
            if (fileChooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { addFiles(it) }
        }
        add(dir)
        add(hidden)
        val clear = JMenuItem("Clear")
        clear.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK)
        clear.setMnemonic('C')
        clear.addActionListener { TableModel.clear() }
        add(clear)
        add(JSeparator())
        val close = JMenuItem("Close tab")
        close.setMnemonic('t')
        close.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK)
        close.addActionListener { MainController.closeTab() }
        add(close)
    }

    private fun buildFileChooser(showHidden: Boolean): JFileChooser {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
        fileChooser.isMultiSelectionEnabled = true
        fileChooser.isFileHidingEnabled = !showHidden
        return fileChooser
    }

    private fun addFiles(file: File) {
        if (file.isFile)
            TableModel.add(file)
        else if (file.isDirectory)
            file.listFiles().forEach { addFiles(it) }
    }
}