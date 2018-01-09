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
        tagView.addActionListener { MainController.tagsView() }
        add(tagView)
    }
}