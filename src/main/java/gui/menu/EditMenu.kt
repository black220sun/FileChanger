package gui.menu

import filechanger.FileChangerContainer
import gui.MainController
import gui.TableModel
import settings.Settings
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import java.io.File

class EditMenu : JMenu(Settings.getLang("Edit")) {
    private val changer = TableModel.changer
    init {
        changer.init()
        setMnemonic('E')

        val force = JCheckBoxMenuItem(Settings.getLang("Force rename"), false)
        force.setMnemonic('F')

        val translate = JMenuItem(Settings.getLang("Translate"))
        translate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK)
        translate.setMnemonic('T')
        translate.addActionListener {
            val files = changer.getFiles()
            val new = changer.translate(force = force.state)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(translate)

        val rename = JMenuItem(Settings.getLang("Rename"))
        rename.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK)
        rename.setMnemonic('R')
        rename.addActionListener {
            val files = changer.getFiles()
            val new = changer.translate(force = force.state)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(rename)

        val regex = JMenuItem(Settings.getLang("Regex rename"))
        regex.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK)
        regex.setMnemonic('g')
        regex.addActionListener {
            val files = changer.getFiles()
            val new = changer.rename(force = force.state, regex = true)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(regex)

        add(force)

        add(JSeparator())

        val addTranslate = JMenuItem(Settings.getLang("Add translation"))
        addTranslate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)
        addTranslate.setMnemonic('l')
        addTranslate.addActionListener { getLines(FileChangerContainer.Type.TRANSLATION) }
        add(addTranslate)

        val addReplace = JMenuItem(Settings.getLang("Add rename"))
        addReplace.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK)
        addReplace.setMnemonic('n')
        addReplace.addActionListener { getLines(FileChangerContainer.Type.REPLACEMENT) }
        add(addReplace)

        val addRegex = JMenuItem(Settings.getLang("Add regex"))
        addRegex.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK)
        addRegex.setMnemonic('g')
        addRegex.addActionListener { getLines(FileChangerContainer.Type.REGEX) }
        add(addRegex)

        add(JSeparator())

        val clearTranslate = JMenuItem(Settings.getLang("Clear translation"))
        clearTranslate.addActionListener { changer.clear(FileChangerContainer.Type.TRANSLATION) }
        add(clearTranslate)

        val clearReplace = JMenuItem(Settings.getLang("Clear rename"))
        clearReplace.addActionListener { changer.clear(FileChangerContainer.Type.REPLACEMENT) }
        add(clearReplace)

        val clearRegex = JMenuItem(Settings.getLang("Clear regex"))
        clearRegex.addActionListener { changer.clear(FileChangerContainer.Type.REGEX) }
        add(clearRegex)

        add(JSeparator())

        val show = JMenuItem(Settings.getLang("Show"))
        show.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK)
        show.setMnemonic('S')
        show.addActionListener {
            val repl = changer.getReplacements()
            MainController.replacements(repl)
        }
        add(show)

        add(JSeparator())

        val loadTranslate = JMenuItem(Settings.getLang("Load translation"))
        loadTranslate.addActionListener {  load(FileChangerContainer.Type.TRANSLATION) }
        add(loadTranslate)

        val loadReplace = JMenuItem(Settings.getLang("Load rename"))
        loadReplace.addActionListener { load(FileChangerContainer.Type.REPLACEMENT) }
        add(loadReplace)

        val loadRegex = JMenuItem(Settings.getLang("Load regex"))
        loadRegex.addActionListener { load(FileChangerContainer.Type.REGEX) }
        add(loadRegex)
    }

    private fun getLines(table: FileChangerContainer.Type) {
        val from = JOptionPane.showInputDialog(parent.parent, Settings.getLang("From:"), Settings.getLang("Replace"), JOptionPane.QUESTION_MESSAGE)
        if (from == null || from.isEmpty())
            return
        val to: String = JOptionPane.showInputDialog(parent.parent, Settings.getLang("To:"), Settings.getLang("Replace"), JOptionPane.QUESTION_MESSAGE) ?: return
        changer.add(from, to, table)
    }

    private fun load(table: FileChangerContainer.Type) {
        val chooser = JFileChooser(File("."))
        if (chooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
            changer.load(chooser.selectedFile.absolutePath, table)
    }
}