package gui.tabs

import settings.Settings
import javax.swing.*

class SettingsTab : JPanel() {
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        add(panel)

        panel.add(JLabel(Settings.getLang("Language:")))
        val lang = JComboBox(Settings.languages.keys.toTypedArray())
        lang.selectedItem = Settings.getProperty("langName")!!
        lang.addActionListener {
            Settings.setProperty("langName", lang.selectedItem as String)
        }
        panel.add(lang)

        val process = JButton(Settings.getLang("Save"))
        process.addActionListener {
            Settings.save()
        }
        panel.add(process)
    }
}