package gui2

import gui2.toolbar.ToolBar
import mp3tag.TagReader
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
    private val toolbar = ToolBar()
    private val table = FileTable(model)
    init {
//        jMenuBar = MenuBar()
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        preferredSize = Dimension(1000, 600)

        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        contentPane.add(toolbar)
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(files)
        panel.add(TableView(table))
        contentPane.add(panel)

        pack()
        addWindowListener(this)
        load()
        isVisible = true
        extendedState = JFrame.MAXIMIZED_BOTH
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
        table.load()
    }

    private fun quit() {
        if (Settings.getSaveLoad())
            model.saveFiles(Settings.getSaveLoadPath())
        table.save()
        Settings.saveLang()
        dispose()
    }

    fun show(dir: File) = model.showDir(dir)
    fun addFiles() {
        model.clear(false)
        files.getSelected().forEach { model.addDir(it) }
    }
    fun clearFiles(selected: Boolean) = model.clear(selected)
    fun selectAll(state: Boolean) = model.selectAll(state)
    fun setRoot(file: File) = files.setRoot(file)
    fun createFolder() {
        val selected = files.getSelected()
        if (selected.size != 1) {
            JOptionPane.showMessageDialog(this, Settings.getLang("Choose exactly one parent folder"))
            return
        }
        val name = JOptionPane.showInputDialog(this, Settings.getLang("Directory name:"))
        if (name.isNullOrBlank())
            return
        val new = File(selected[0].absolutePath + File.separator + name)
        new.mkdir()
        files.update()
    }

    fun deleteFolder() {
        val selected = files.getSelected()
        if (selected.isEmpty())
            return
        if (JOptionPane.showConfirmDialog(this, Settings.getLang("Delete selected folders?"),
                Settings.getLang("Confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            selected.forEach { it.deleteRecursively() }
        files.update()
    }

    fun fillTags(tags: TagReader.TagsData) = toolbar.fillTags(tags)
    fun select(filter: List<String>, ext: Boolean) = model.select(filter, ext)
    fun save() = model.save()
}