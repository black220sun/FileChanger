package gui2.toolbar

import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import settings.Settings
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JScrollPane

class SettingsTab: JScrollPane() {
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        viewport.view = panel

        panel.add(LLabel("Language:"))
        val lang = JComboBox(Settings.getLanguages())
        lang.selectedItem = Settings.getLangName()
        lang.maximumSize = Dimension(2000, 24)
        panel.add(lang)

        val forceQuit = LCheckBox("Quit", Settings.getForce("forceQuit"))
        panel.add(forceQuit)

        val save = LCheckBox("Save files on quit", Settings.getSaveLoad())
        panel.add(save)

        val process = LButton("Save")
        process.addActionListener {
            Settings.setLangName(lang.selectedItem as String)
            Settings.setForce("forceQuit", forceQuit.isSelected)
            Settings.setSaveLoad(save.isSelected)
            Settings.save()
        }
        panel.add(process)
    }
}