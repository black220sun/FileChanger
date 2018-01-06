package gui.tabs

import gui.MainController
import gui.TableModel
import javax.swing.*

class CapitalizeTab : JPanel() {
    private val changer = TableModel.changer
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(JLabel("Delimiter"))
        val delimiter = JTextField(" - ")
        panel.add(delimiter)
        val allBefore = JCheckBox("Capitalize all words before delimiter", true)
        panel.add(allBefore)
        panel.add(JLabel("Words delimiter"))
        val before = JTextField(" ")
        panel.add(before)
        val force = JCheckBox("Force rename", false)
        panel.add(force)
        val process = JButton("Capitalize")
        process.addActionListener {
            val files = changer.getFiles()
            val new = changer.capitalize({true}, delimiter.text, allBefore.isSelected, before.text, force.isSelected)
            MainController.results(arrayListOf(files, new), force.isSelected)
        }
        panel.add(process)
        add(panel)
    }
}
