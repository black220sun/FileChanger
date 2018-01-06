package gui

import gui.tabs.ReplacementsTab
import gui.tabs.ResultsTab
import java.io.File

object MainController {
    private val view = MainView()

    fun show() {
        view.isVisible = true
    }
    fun results(files: Collection<Collection<File>>) {
        view.tabPanel.addTab("Results", ResultsTab(files))
    }

    fun closeTab() {
        val selected = view.tabPanel.selectedIndex
        if (selected != 0)
            view.tabPanel.removeTabAt(selected)
    }

    fun replacements(replacements: ArrayList<ArrayList<String>>) {
        view.tabPanel.addTab("Replacements", ReplacementsTab(replacements))
    }
}
