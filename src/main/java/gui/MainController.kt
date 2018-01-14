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
        if (force)
            TableModel.update()
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
        val index = view.tabPanel.indexOfTab("Settings")
        if (index > 0)
            view.tabPanel.selectedIndex = index
        else
            addTab(Settings.getLang("Settings"), SettingsTab())
    }

    fun tags(result: ArrayList<List<Any>>) {
        addTab(Settings.getLang("Results"), TagResultsTab(result))
    }
}
