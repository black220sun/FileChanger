package gui.menu

import gui.MainController
import settings.Settings
import javax.swing.JMenu
import javax.swing.JMenuItem

class RenameMenu : JMenu(Settings.getLang("Rename")) {
    init {
        setMnemonic('R')

        val cap = JMenuItem(Settings.getLang("Capitalize"))
        cap.setMnemonic('C')
        cap.addActionListener { MainController.capitalization() }
        add(cap)
    }
}