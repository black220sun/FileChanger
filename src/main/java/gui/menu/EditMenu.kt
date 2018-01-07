package gui.menu

import filechanger.FileChangerContainer
import gui.MainController
import gui.TableModel
import gui.util.LMenu
import gui.util.LMenuItem
import settings.Settings
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*
import java.io.File

class EditMenu : LMenu("Edit") {
    private val changer = TableModel.changer
    init {
        changer.init()
        setMnemonic('E')

        val translate = LMenuItem("Translate")
        translate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK)
        translate.setMnemonic('T')
        translate.addActionListener {
            val files = changer.getFiles()
            val force = Settings.getForce("forceRename")
            val new = changer.translate(force = force)
            MainController.results(arrayListOf(files, new), force)
        }
        add(translate)

        val rename = LMenuItem("Rename")
        rename.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK)
        rename.setMnemonic('R')
        rename.addActionListener {
            val files = changer.getFiles()
            val force = Settings.getForce("forceRename")
            val new = changer.translate(force = force)
            MainController.results(arrayListOf(files, new), force)
        }
        add(rename)

        val regex = LMenuItem("Regex rename")
        regex.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK)
        regex.setMnemonic('g')
        regex.addActionListener {
            val files = changer.getFiles()
            val force = Settings.getForce("forceRename")
            val new = changer.rename(force = force, regex = true)
            MainController.results(arrayListOf(files, new), force)
        }
        add(regex)

        add(JSeparator())

        val addTranslate = LMenuItem("Add translation")
        addTranslate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)
        addTranslate.setMnemonic('l')
        addTranslate.addActionListener { getLines(FileChangerContainer.Type.TRANSLATION) }
        add(addTranslate)

        val addReplace = LMenuItem("Add rename")
        addReplace.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK)
        addReplace.setMnemonic('n')
        addReplace.addActionListener { getLines(FileChangerContainer.Type.REPLACEMENT) }
        add(addReplace)

        val addRegex = LMenuItem("Add regex")
        addRegex.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK)
        addRegex.setMnemonic('g')
        addRegex.addActionListener { getLines(FileChangerContainer.Type.REGEX) }
        add(addRegex)

        add(JSeparator())

        val clearTranslate = LMenuItem("Clear translation")
        clearTranslate.addActionListener { changer.clear(FileChangerContainer.Type.TRANSLATION) }
        add(clearTranslate)

        val clearReplace = LMenuItem("Clear rename")
        clearReplace.addActionListener { changer.clear(FileChangerContainer.Type.REPLACEMENT) }
        add(clearReplace)

        val clearRegex = LMenuItem("Clear regex")
        clearRegex.addActionListener { changer.clear(FileChangerContainer.Type.REGEX) }
        add(clearRegex)

        add(JSeparator())

        val show = LMenuItem("Show")
        show.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK)
        show.setMnemonic('S')
        show.addActionListener {
            val repl = changer.getReplacements()
            MainController.replacements(repl)
        }
        add(show)

        add(JSeparator())

        val loadTranslate = LMenuItem("Load translation")
        loadTranslate.addActionListener {  load(FileChangerContainer.Type.TRANSLATION) }
        add(loadTranslate)

        val loadReplace = LMenuItem("Load rename")
        loadReplace.addActionListener { load(FileChangerContainer.Type.REPLACEMENT) }
        add(loadReplace)

        val loadRegex = LMenuItem("Load regex")
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