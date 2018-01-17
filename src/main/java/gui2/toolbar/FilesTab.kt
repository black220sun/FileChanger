package gui2.toolbar

import settings.Settings
import gui.util.LIcon
import gui.util.LLabel
import gui2.MainView
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.JPanel
import java.io.File
import javax.swing.JComboBox

class FilesTab : JPanel() {
    init {
        val panel = JPanel()
        panel.layout = GridLayout(3, 3)

        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        preferredSize = Dimension(160, 160)
        minimumSize = preferredSize
        maximumSize = preferredSize

        val roots = File.listRoots() + File(Settings.home)
        val comboBox = JComboBox(roots)
        comboBox.selectedIndex = roots.lastIndex
        comboBox.addActionListener {
            MainView.setRoot(comboBox.selectedItem as File)
        }
        val size = Dimension(160,24)
        comboBox.preferredSize = size
        comboBox.maximumSize = size
        comboBox.minimumSize = size


        val create = LIcon("createFolder.png")
        create.addActionListener { MainView.createFolder() }

        val delete = LIcon("deleteFolder.png")
        delete.addActionListener { MainView.deleteFolder() }

        val open = LIcon("open.png")
        open.addActionListener { MainView.addFiles() }

        val clear = LIcon("clear.png")
        clear.addActionListener { MainView.clearFiles(false) }

        val clearSelected = LIcon("clearSelected.png")
        clearSelected.addActionListener { MainView.clearFiles(true) }

        val selectAll = LIcon("selectAll.png")
        selectAll.addActionListener { MainView.selectAll(true) }

        val cancelAll = LIcon("cancelAll.png")
        cancelAll.addActionListener { MainView.selectAll(false) }

        val save = LIcon("save.png")
        save.addActionListener { MainView.save() }

        val invert = LIcon("invert.png")
        invert.addActionListener { MainView.invert() }

        add(panel)
        add(comboBox)

        panel.add(create)
        panel.add(delete)
        panel.add(save)

        panel.add(open)
        panel.add(clear)
        panel.add(clearSelected)

        panel.add(selectAll)
        panel.add(cancelAll)
        panel.add(invert)
    }
}