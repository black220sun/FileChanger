package gui2.toolbar

import settings.Settings
import gui.util.LIcon
import gui2.MainView
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import java.io.File
import javax.swing.JComboBox

class FilesTab : JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)

        val roots = File.listRoots() + File(Settings.home)
        val comboBox = JComboBox(roots)
        comboBox.selectedIndex = roots.lastIndex
        comboBox.addActionListener {
            MainView.setRoot(comboBox.selectedItem as File)
        }
        val size = Dimension(140,32)
        comboBox.preferredSize = size
        comboBox.maximumSize = size
        comboBox.minimumSize = size
        add(comboBox)

        val create = LIcon("createFolder.png")
        create.addActionListener { MainView.createFolder() }
        add(create)

        val delete = LIcon("deleteFolder.png")
        delete.addActionListener { MainView.deleteFolder() }
        add(delete)

        val open = LIcon("open.png")
        open.addActionListener { MainView.addFiles() }
        add(open)

        val clear = LIcon("clear.png")
        clear.addActionListener { MainView.clearFiles(false) }
        add(clear)

        val clearSelected = LIcon("clearSelected.png")
        clearSelected.addActionListener { MainView.clearFiles(true) }
        add(clearSelected)

        val selectAll = LIcon("selectAll.png")
        selectAll.addActionListener { MainView.selectAll(true) }
        add(selectAll)

        val cancelAll = LIcon("cancelAll.png")
        cancelAll.addActionListener { MainView.selectAll(false) }
        add(cancelAll)

        val save = LIcon("save.png")
        save.addActionListener { MainView.save() }
        add(save)

    }
}