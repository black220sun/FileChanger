package gui

import gui.tabs.ReplacementsTab
import gui.tabs.ResultsTab
import java.io.File
import javax.swing.JComponent

object MainController {
    private val view = MainView()

    fun show() {
        view.isVisible = true
    }

    private fun addTab(title: String, component: JComponent) {
        view.tabPanel.addTab(title,component)
        view.tabPanel.selectedIndex = view.tabPanel.indexOfComponent(component)
    }

    fun results(files: Collection<Collection<File>>, force: Boolean) {
        addTab("Results", ResultsTab(files))
        if (force)
            TableModel.update()
    }

    fun closeTab() {
        val selected = view.tabPanel.selectedIndex
        if (selected != 0)
            view.tabPanel.removeTabAt(selected)
    }

    fun replacements(replacements: ArrayList<ArrayList<String>>) {
        addTab("Replacements", ReplacementsTab(replacements))
    }
}
