package gui.tabs

import gui.MainController
import gui.TableModel
import settings.Settings
import javax.swing.*

class CapitalizeTab : JPanel() {
    private val changer = TableModel.changer
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(JLabel(Settings.getLang("Delimiter")))
        val delimiter = JTextField(" - ")
        panel.add(delimiter)
        val allBefore = JCheckBox(Settings.getLang("Capitalize all words before delimiter"), true)
        panel.add(allBefore)
        panel.add(JLabel(Settings.getLang("Words delimiter")))
        val before = JTextField(" ")
        panel.add(before)
        val force = JCheckBox(Settings.getLang("Force rename"), false)
        panel.add(force)
        val process = JButton(Settings.getLang("Capitalize"))
        process.addActionListener {
            val files = changer.getFiles()
            val new = changer.capitalize({true}, delimiter.text, allBefore.isSelected, before.text, force.isSelected)
            MainController.results(arrayListOf(files, new), force.isSelected)
        }
        panel.add(process)
        add(panel)
    }
}
