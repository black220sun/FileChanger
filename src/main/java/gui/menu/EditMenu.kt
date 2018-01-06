package gui.menu

import filechanger.FileChangerContainer
import gui.MainController
import gui.TableModel
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.*

class EditMenu : JMenu("Edit") {
    private val changer = FileChangerContainer()
    init {
        changer.init()
        setMnemonic('E')
        val translate = JMenuItem("Translate")
        translate.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK)
        translate.setMnemonic('T')
        translate.addActionListener {
            val files = TableModel.files
            changer.addFiles(files)
            val new = changer.translate(force = false)
            MainController.results(arrayListOf(files, new))
        }
        add(translate)
        val rename = JMenuItem("Rename")
        rename.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK)
        rename.setMnemonic('R')
        rename.addActionListener {
            val files = TableModel.files
            changer.addFiles(files)
            val new = changer.rename(force = false)
            MainController.results(arrayListOf(files, new))
        }
        add(rename)
        val regex = JMenuItem("Regex rename")
        regex.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK)
        regex.setMnemonic('g')
        regex.addActionListener {
            val files = TableModel.files
            changer.addFiles(files)
            val new = changer.rename(force = false, regex = true)
            MainController.results(arrayListOf(files, new))
        }
        add(regex)
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
    }

    private fun getLines(translation: FileChangerContainer.Type) {
        val from = JOptionPane.showInputDialog(this, "From:", "Replace") ?: return
        var to = JOptionPane.showInputDialog(this, "To:", "Replace")
        if (to == null)
            to = ""
        changer.add(from, to, translation)
    }
}