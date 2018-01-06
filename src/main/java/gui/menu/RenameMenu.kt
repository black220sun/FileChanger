package gui.menu

import gui.MainController
import javax.swing.JMenu
import javax.swing.JMenuItem

class RenameMenu : JMenu("Rename") {
    init {
        setMnemonic('R')

        val cap = JMenuItem("Capitalize")
        cap.setMnemonic('C')
        cap.addActionListener { MainController.capitalization() }
        add(cap)
    }
}