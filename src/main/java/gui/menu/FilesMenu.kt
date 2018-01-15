package gui.menu

import gui.MainController
import gui.TableModel
import gui.util.LCheckBoxMenuItem
import gui.util.LMenu
import gui.util.LMenuItem
import settings.Settings
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.*

class FilesMenu: LMenu("Files") {
    init {
        setMnemonic('F')

        val hidden = LCheckBoxMenuItem("Show hidden files", false)
        hidden.setMnemonic('h')

        val open = LMenuItem("Open")
        open.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
        open.setMnemonic('O')
        open.addActionListener {
            val fileChooser =  buildFileChooser(hidden.state)
            if (fileChooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { TableModel.add(it) }
        }
        add(open)

        val dir = LMenuItem("Add from directory")
        dir.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK)
        dir.setMnemonic('A')
        dir.addActionListener {
            val fileChooser =  buildFileChooser(hidden.state)
            if (fileChooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { addFiles(it) }
        }
        add(dir)

        add(hidden)

        val clear = LMenuItem("Clear")
        clear.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK)
        clear.setMnemonic('C')
        clear.addActionListener { TableModel.clear() }
        add(clear)

        val deleteSelected = LMenuItem("Delete selected files")
        deleteSelected.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE.toChar())
        deleteSelected.setMnemonic('D')
        deleteSelected.addActionListener { MainController.delete(false) }
        add(deleteSelected)

        val delete = LMenuItem("Delete from memory")
        delete.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK)
        delete.addActionListener {
            if (!Settings.getForce("forceDelete"))
                if (JOptionPane.showConfirmDialog(parent.parent, Settings.getLang("Delete selected files"),
                        Settings.getLang("Delete"), JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION)
                    return@addActionListener
            MainController.delete(true)
        }
        add(delete)

        add(JSeparator())

        val save = LMenuItem("Save files")
        save.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)
        save.setMnemonic('S')
        save.addActionListener { files(true) }
        add(save)

        val load = LMenuItem("Load files")
        load.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK)
        load.setMnemonic('L')
        load.addActionListener { files(false) }
        add(load)

        add(JSeparator())

        val close = LMenuItem("Close tab")
        close.setMnemonic('t')
        close.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK)
        close.addActionListener { MainController.closeTab() }
        add(close)

        val quit = LMenuItem("Quit")
        quit.addActionListener { MainController.close() }
        add(quit)
    }

    private fun files(save: Boolean) {
        val fileChooser = JFileChooser()
        if (fileChooser.showOpenDialog(parent.parent) != JFileChooser.APPROVE_OPTION)
            return
        val path = fileChooser.selectedFile.absolutePath
        if (save) {
            TableModel.changer.saveFiles(path)
            JOptionPane.showMessageDialog(parent.parent, Settings.getLang("Done!"))
        } else {
            TableModel.changer.loadFiles(path)
            TableModel.update()
        }
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