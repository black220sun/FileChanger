package gui.menu

import gui.MainController
import settings.Settings
import javax.swing.JMenu
import javax.swing.JMenuItem

class SettingsMenu : JMenu(Settings.getLang("Settings")) {
    init {
        setMnemonic('S')

        val settings = JMenuItem(Settings.getLang("Settings"))
        settings.setMnemonic('S')
        settings.addActionListener { MainController.settings() }
        add(settings)
    }
}