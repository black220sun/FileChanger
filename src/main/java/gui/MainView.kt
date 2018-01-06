package gui

import gui.menu.MenuBar
import javax.swing.JFrame
import javax.swing.JTabbedPane

class MainView : JFrame("File Changer") {
    init {
        jMenuBar = MenuBar()
        val tabPanel = JTabbedPane()
        val filesTab = FilesTab()
        tabPanel.addTab("Files", filesTab)
        add(tabPanel)
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        pack()
    }
}