package gui2

import java.io.File
import settings.Settings
import java.awt.Dimension
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.*

object MainView : JFrame("Tag Changer"), WindowListener {
    override fun windowDeiconified(p0: WindowEvent?) = Unit
    override fun windowClosed(p0: WindowEvent?) = Unit
    override fun windowActivated(p0: WindowEvent?) = Unit
    override fun windowDeactivated(p0: WindowEvent?) = Unit
    override fun windowOpened(p0: WindowEvent?) = Unit
    override fun windowIconified(p0: WindowEvent?) = Unit
    override fun windowClosing(p0: WindowEvent?) = close()

    private val model = FileModel()
    private val files = FileTree(File(Settings.home))
    init {
//        jMenuBar = MenuBar()
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        preferredSize = Dimension(1000, 600)

        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        contentPane.add(ToolBar())
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(files)
        panel.add(TableView(FileTable(model)))
        contentPane.add(panel)

        pack()
        addWindowListener(this)
        load()
        isVisible = true
    }

    fun close() {
        if (Settings.getForce("forceQuit")) {
            quit()
            return
        }
        val result = JOptionPane.showConfirmDialog(this, Settings.getLang("Quit?"), "", JOptionPane.YES_NO_OPTION)
        if (result == JOptionPane.YES_OPTION) {
            quit()
        }
    }

    private fun load() {
        if (Settings.getSaveLoad()) {
            model.loadFiles(Settings.getSaveLoadPath())
        }
    }

    private fun quit() {
        if (Settings.getSaveLoad())
            model.saveFiles(Settings.getSaveLoadPath())
        Settings.saveLang()
        dispose()
    }

    fun addFiles() = files.getSelected().forEach { model.addDir(it) }

    fun clearFiles(selected: Boolean) = model.clear(selected)
}