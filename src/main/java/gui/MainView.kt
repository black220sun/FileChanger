package gui

import gui.menu.MenuBar
import gui.tabs.FilesTab
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JTabbedPane

class MainView : JFrame("File Changer") {
    val tabPanel = JTabbedPane()
    init {
        jMenuBar = MenuBar()
        val filesTab = FilesTab()
        tabPanel.addTab("Files", filesTab)
        add(tabPanel)
        defaultCloseOperation = EXIT_ON_CLOSE
        preferredSize = Dimension(1000, 600)
        pack()
    }
}