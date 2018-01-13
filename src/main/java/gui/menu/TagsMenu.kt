package gui.menu

import gui.MainController
import gui.util.LMenu
import gui.util.LMenuItem
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class TagsMenu : LMenu("Tags") {
    init {
        setMnemonic('T')

        val tagView = LMenuItem("Tags view")
        tagView.setMnemonic('T')
        tagView.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)
        tagView.addActionListener { MainController.addTab("Mp3 info") }
        add(tagView)

        val process = LMenuItem("Tags processing")
        process.setMnemonic('p')
        process.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK)
        process.addActionListener { MainController.addTab("Tags processing") }
        add(process)
    }
}