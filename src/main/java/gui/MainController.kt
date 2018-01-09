package gui

import gui.tabs.*
import settings.Settings
import java.io.File
import javax.swing.JComponent

object MainController {
    private val view = MainView()

    fun show() {
        view.isVisible = true
    }

    fun close() {
        view.close()
    }

    private fun addTab(title: String, component: JComponent) {
        view.tabPanel.addTab(title,component)
        view.tabPanel.selectedIndex = view.tabPanel.indexOfComponent(component)
    }

    fun results(files: Collection<Collection<File>>, force: Boolean) {
        addTab(Settings.getLang("Results"), ResultsTab(files))
        if (force)
            TableModel.update()
    }

    fun closeTab() {
        val selected = view.tabPanel.selectedIndex
        if (selected != 0)
            view.tabPanel.removeTabAt(selected)
    }

    fun replacements(replacements: ArrayList<ArrayList<String>>) {
        addTab(Settings.getLang("Replacements"), ReplacementsTab(replacements))
    }

    fun capitalization() {
        addTab(Settings.getLang("Capitalize"), CapitalizeTab())
    }


    fun tagsView() {
        addTab(Settings.getLang("Mp3 info"), TagsViewTab())
    }

    fun settings() {
        val index = view.tabPanel.indexOfTab("Settings")
        if (index > 0)
            view.tabPanel.selectedIndex = index
        else
            addTab(Settings.getLang("Settings"), SettingsTab())
    }

}
