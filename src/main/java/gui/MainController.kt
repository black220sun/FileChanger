package gui

import gui.tabs.*
import settings.Settings
import java.io.File
import javax.swing.JComponent

object MainController {
    private val view = MainView()
    private val tabs = HashMap<String, Class<JComponent>>()

    init {
        registerTab("Mp3 info", Class.forName("gui.tabs.TagsViewTab"))
        registerTab("Capitalize", Class.forName("gui.tabs.CapitalizeTab"))
        registerTab("Tags processing", Class.forName("gui.tabs.TagsProcessingTab"))
    }

    fun show() {
        view.isVisible = true
    }

    fun close() {
        view.close()
    }

    private fun addTab(title: String, tab: JComponent) {
        view.tabPanel.addTab(title, tab)
        view.tabPanel.selectedIndex = view.tabPanel.indexOfComponent(tab)
    }

    fun results(files: Collection<Collection<File>>, force: Boolean) {
        addTab(Settings.getLang("Results"), ResultsTab(files))
        if (force) {
            TableModel.changer.removeFiles { it in files.elementAt(0) }
            TableModel.changer.addFiles(files.elementAt(1))
            TableModel.update()
        }
    }

    fun addTab(title: String) {
        val tab = tabs[title] ?: return
        addTab(Settings.getLang(title), tab.newInstance())
    }

    private fun registerTab(title: String, tab: Class<*>) {
        try {
            @Suppress("UNCHECKED_CAST")
            tab as Class<JComponent>
        } catch (e: Exception) {
            return
        }
        tabs.put(title, tab)
    }

    fun closeTab() {
        val selected = view.tabPanel.selectedIndex
        if (selected != 0)
            view.tabPanel.removeTabAt(selected)
    }

    fun replacements(replacements: ArrayList<ArrayList<String>>) {
        addTab(Settings.getLang("Replacements"), ReplacementsTab(replacements))
    }

    fun settings() {
        val index = view.tabPanel.indexOfTab(Settings.getLang("Settings"))
        if (index > 0)
            view.tabPanel.selectedIndex = index
        else
            addTab(Settings.getLang("Settings"), SettingsTab())
    }

    fun tags(result: ArrayList<List<Any>>) {
        addTab(Settings.getLang("Results"), TagResultsTab(result))
    }

    fun delete(force: Boolean) {
        val index = view.tabPanel.selectedIndex
        when (index) {
            view.tabPanel.indexOfTab(Settings.getLang("Files")) -> delFiles(index, force)
            view.tabPanel.indexOfTab(Settings.getLang("Results")) -> delResults(index, force)
            view.tabPanel.indexOfTab(Settings.getLang("Mp3 info")) -> delInfo(index, force)
        }
    }

    private fun delResults(i: Int, force: Boolean) {
        (view.tabPanel.getComponentAt(i) as ResultsTab).delete(force)
    }

    private fun delInfo(i: Int, force: Boolean) {
        (view.tabPanel.getComponentAt(i) as TagsViewTab).delete(force)
    }

    private fun delFiles(i: Int, force: Boolean) {
        (view.tabPanel.getComponentAt(i) as FilesTab).delete(force)
    }
}
