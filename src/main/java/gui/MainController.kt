package gui

import gui.tabs.ReplacementsTab
import gui.tabs.ResultsTab
import gui.tabs.CapitalizeTab
import gui.tabs.SettingsTab
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

    fun settings() {
        addTab(Settings.getLang("Settings"), SettingsTab())
    }
}
