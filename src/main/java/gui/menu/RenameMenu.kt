package gui.menu

import gui.MainController
import gui.util.LMenu
import gui.util.LMenuItem

class RenameMenu : LMenu("Rename") {
    init {
        setMnemonic('R')

        val cap = LMenuItem("Capitalize")
        cap.setMnemonic('C')
        cap.addActionListener { MainController.addTab("Capitalize") }
        add(cap)
    }
}