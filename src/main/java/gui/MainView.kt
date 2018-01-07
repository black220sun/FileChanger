package gui

import gui.menu.MenuBar
import gui.tabs.FilesTab
import settings.Settings
import java.awt.Dimension
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JTabbedPane
import javax.swing.WindowConstants

class MainView : JFrame(Settings.getLang("File Changer")), WindowListener {
    override fun windowDeiconified(p0: WindowEvent?) = Unit
    override fun windowClosed(p0: WindowEvent?) = Unit
    override fun windowActivated(p0: WindowEvent?) = Unit
    override fun windowDeactivated(p0: WindowEvent?) = Unit
    override fun windowOpened(p0: WindowEvent?) = Unit
    override fun windowIconified(p0: WindowEvent?) = Unit
    override fun windowClosing(p0: WindowEvent?) = close()

    val tabPanel = JTabbedPane()
    init {
        jMenuBar = MenuBar()
        val filesTab = FilesTab()
        tabPanel.addTab(Settings.getLang("Files"), filesTab)
        add(tabPanel)
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        preferredSize = Dimension(1000, 600)
        pack()
        addWindowListener(this)
    }

    fun close() {
        val result = JOptionPane.showConfirmDialog(this, Settings.getLang("Quit?"), "", JOptionPane.YES_NO_OPTION)
        if (result == JOptionPane.OK_OPTION || result == JOptionPane.YES_OPTION) {
            Settings.saveLang()
            dispose()
        }
    }
}