package gui.tabs

import gui.MainController
import gui.TableModel
import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import settings.Settings
import javax.swing.*

class CapitalizeTab : JPanel() {
    private val changer = TableModel.changer
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(LLabel("Delimiter"))
        val delimiter = JTextField(" - ")
        panel.add(delimiter)
        val allBefore = LCheckBox("Capitalize all words before delimiter", true)
        panel.add(allBefore)
        panel.add(LLabel("Words delimiter"))
        val before = JTextField(" ")
        panel.add(before)
        val process = LButton("Capitalize")
        process.addActionListener {
            val files = changer.getFiles()
            val force = Settings.getForce("forceRename")
            val new = changer.capitalize({true}, delimiter.text, allBefore.isSelected, before.text, force)
            MainController.results(arrayListOf(files, new), force)
        }
        panel.add(process)
        add(panel)
    }
}
