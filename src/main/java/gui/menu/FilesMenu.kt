package gui.menu

import gui.MainController
import gui.TableModel
import settings.Settings
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.*

class FilesMenu: JMenu(Settings.getLang("Files")) {
    init {
        setMnemonic('F')

        val hidden = JCheckBoxMenuItem(Settings.getLang("Show hidden files"), false)
        hidden.setMnemonic('h')

        val open = JMenuItem(Settings.getLang("Open"))
        open.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)
        open.setMnemonic('O')
        open.addActionListener {
            val fileChooser =  buildFileChooser(hidden.state)
            if (fileChooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { TableModel.add(it) }
        }
        add(open)

        val dir = JMenuItem(Settings.getLang("Add from directory"))
        dir.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK)
        dir.setMnemonic('A')
        dir.addActionListener {
            val fileChooser =  buildFileChooser(hidden.state)
            if (fileChooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
                fileChooser.selectedFiles.forEach { addFiles(it) }
        }
        add(dir)

        add(hidden)

        val clear = JMenuItem(Settings.getLang("Clear"))
        clear.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK)
        clear.setMnemonic('C')
        clear.addActionListener { TableModel.clear() }
        add(clear)

        add(JSeparator())

        val save = JMenuItem(Settings.getLang("Save files"))
        save.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)
        save.setMnemonic('S')
        save.addActionListener { files(true) }
        add(save)

        val load = JMenuItem(Settings.getLang("Load files"))
        load.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK)
        load.setMnemonic('L')
        load.addActionListener { files(false) }
        add(load)

        add(JSeparator())

        val close = JMenuItem(Settings.getLang("Close tab"))
        close.setMnemonic('t')
        close.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK)
        close.addActionListener { MainController.closeTab() }
        add(close)

        val quit = JMenuItem(Settings.getLang("Quit"))
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