package gui.menu

import gui.MainController
import gui.util.LMenu
import gui.util.LMenuItem
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke

class SettingsMenu : LMenu("Settings") {
    init {
        setMnemonic('S')

        val settings = LMenuItem("Settings")
        settings.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK)
        settings.setMnemonic('S')
        settings.addActionListener { MainController.settings() }
        add(settings)
    }
}