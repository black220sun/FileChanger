package gui2.toolbar

import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import settings.Settings
import java.awt.Dimension
import javax.swing.*

class SettingsTab: JScrollPane() {
    init {
        val size = Dimension(200, 24)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        viewport.view = panel

        panel.add(LLabel("Language:"))

        val lang = JComboBox(Settings.getLanguages())
        lang.selectedItem = Settings.getLangName()
        lang.maximumSize = size
        panel.add(lang)

        panel.add(LLabel("Font size:"))

        val fontInit = Settings.getSize("sizeFont") ?: 12
        val fontSize = JSpinner(SpinnerNumberModel(fontInit, 8, 32, 2))
        fontSize.maximumSize = size
        panel.add(fontSize)
        
        panel.add(LLabel("Row size:"))

        val rowInit = Settings.getSize("sizeRow") ?: 14
        val rowSize = JSpinner(SpinnerNumberModel(rowInit, 8, 40, 2))
        rowSize.maximumSize = size
        panel.add(rowSize)

        val forceQuit = LCheckBox("Quit", Settings.getForce("forceQuit"))
        panel.add(forceQuit)

        val save = LCheckBox("Save files on quit", Settings.getSaveLoad())
        panel.add(save)

        val process = LButton("Save")
        process.addActionListener {
            Settings.setLangName(lang.selectedItem as String)
            Settings.setForce("forceQuit", forceQuit.isSelected)
            Settings.setSaveLoad(save.isSelected)
            Settings.setSize("sizeFont", fontSize.value as Int)
            Settings.setSize("sizeRow", rowSize.value as Int)
            Settings.save()
        }
        panel.add(process)
    }
}