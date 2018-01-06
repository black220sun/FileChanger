package gui.menu

import filechanger.FileChangerContainer
import gui.MainController
import gui.TableModel
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import java.io.File

class EditMenu : JMenu("Edit") {
    private val changer = TableModel.changer
    init {
        changer.init()
        setMnemonic('E')

        val force = JCheckBoxMenuItem("Force rename", false)
        force.setMnemonic('F')

        val translate = JMenuItem("Translate")
        translate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK)
        translate.setMnemonic('T')
        translate.addActionListener {
            val files = changer.getFiles()
            val new = changer.translate(force = force.state)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(translate)

        val rename = JMenuItem("Rename")
        rename.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK)
        rename.setMnemonic('R')
        rename.addActionListener {
            val files = changer.getFiles()
            val new = changer.translate(force = force.state)
            MainController.results(arrayListOf(files, new), force.state)
        }
        add(rename)

        val regex = JMenuItem("Regex rename")
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

        val addTranslate = JMenuItem("Add translation")
        addTranslate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)
        addTranslate.setMnemonic('l')
        addTranslate.addActionListener { getLines(FileChangerContainer.Type.TRANSLATION) }
        add(addTranslate)

        val addReplace = JMenuItem("Add rename")
        addReplace.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK)
        addReplace.setMnemonic('n')
        addReplace.addActionListener { getLines(FileChangerContainer.Type.REPLACEMENT) }
        add(addReplace)

        val addRegex = JMenuItem("Add regex")
        addRegex.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK)
        addRegex.setMnemonic('g')
        addRegex.addActionListener { getLines(FileChangerContainer.Type.REGEX) }
        add(addRegex)

        add(JSeparator())

        val clearTranslate = JMenuItem("Clear translation")
        clearTranslate.addActionListener { changer.clear(FileChangerContainer.Type.TRANSLATION) }
        add(clearTranslate)

        val clearReplace = JMenuItem("Clear rename")
        clearReplace.addActionListener { changer.clear(FileChangerContainer.Type.REPLACEMENT) }
        add(clearReplace)

        val clearRegex = JMenuItem("Clear regex")
        clearRegex.addActionListener { changer.clear(FileChangerContainer.Type.REGEX) }
        add(clearRegex)

        add(JSeparator())

        val show = JMenuItem("Show")
        show.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK)
        show.setMnemonic('S')
        show.addActionListener {
            val repl = changer.getReplacements()
            MainController.replacements(repl)
        }
        add(show)

        add(JSeparator())

        val loadTranslate = JMenuItem("Load translation")
        loadTranslate.addActionListener {  load(FileChangerContainer.Type.TRANSLATION) }
        add(loadTranslate)

        val loadReplace = JMenuItem("Load rename")
        loadReplace.addActionListener { load(FileChangerContainer.Type.REPLACEMENT) }
        add(loadReplace)

        val loadRegex = JMenuItem("Load regex")
        loadRegex.addActionListener { load(FileChangerContainer.Type.REGEX) }
        add(loadRegex)
    }

    private fun getLines(table: FileChangerContainer.Type) {
        val from = JOptionPane.showInputDialog(parent.parent, "From:", "Replace", JOptionPane.QUESTION_MESSAGE)
        if (from == null || from.isEmpty())
            return
        val to: String? = JOptionPane.showInputDialog(parent.parent, "To:", "Replace", JOptionPane.QUESTION_MESSAGE) ?: return
        changer.add(from, to!!, table)
    }

    private fun load(table: FileChangerContainer.Type) {
        val chooser = JFileChooser()
        if (chooser.showOpenDialog(parent.parent) == JFileChooser.APPROVE_OPTION)
            changer.load(chooser.selectedFile.absolutePath, table)
    }
}