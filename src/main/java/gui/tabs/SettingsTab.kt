package gui.tabs

import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import settings.Settings as st
import javax.swing.*

class SettingsTab : JPanel() {
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        add(panel)

        panel.add(LLabel("Language:"))
        val lang = JComboBox(st.getLanguages())
        lang.selectedItem = st.getLangName()
        panel.add(lang)

        panel.add(LLabel("Force actions"))
        val forceAll = LCheckBox("All", st.getForce("forceAll"))
        val forceRename = LCheckBox("Rename", st.getForce("forceRename"))
        val forceMove = LCheckBox("Move", st.getForce("forceMove"))
        val forceTag = LCheckBox("Tags processing", st.getForce("forceTag"))
        val forceDelete = LCheckBox("Delete", st.getForce("forceDelete"))
        val forceQuit = LCheckBox("Quit", st.getForce("forceQuit"))
        val forces = arrayOf(forceRename, forceMove, forceTag, forceDelete, forceQuit)
        panel.add(forceAll)
        forceAll.addActionListener {
            forces.forEach {
                if (forceAll.isSelected)
                    it.isSelected = true
                it.isEnabled = !forceAll.isSelected
            }
        }
        forces.forEach {
            it.isEnabled = !forceAll.isSelected
            panel.add(it)
        }

        val save = LCheckBox("Save files on quit", st.getSaveLoad())
        panel.add(save)

        val process = LButton("Save")
        process.addActionListener {
            st.setLangName(lang.selectedItem as String)
            st.setForce("forceAll", forceAll.isSelected)
            st.setForce("forceRename", forceRename.isSelected)
            st.setForce("forceMove", forceMove.isSelected)
            st.setForce("forceTag", forceTag.isSelected)
            st.setForce("forceQuit", forceQuit.isSelected)
            st.setForce("forceDelete", forceDelete.isSelected)
            st.setSaveLoad(save.isSelected)
            st.save()
        }
        panel.add(process)
    }
}