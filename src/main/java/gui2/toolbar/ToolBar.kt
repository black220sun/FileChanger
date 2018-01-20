package gui2.toolbar

import mp3tag.TagReader
import settings.Settings
import java.awt.Component
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTabbedPane

class ToolBar : JPanel() {
    private val tagsTab = TagsTab()
    private val tabs = JTabbedPane()
    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        minimumSize = Dimension(600, 140)

        add(FilesTab())
        add(tabs)

        addTab("Filter", FilterTab())
        addTab("Replace", ReplaceTab(false))
        addTab("Translate", ReplaceTab(true))
        addTab("Tags", tagsTab)
        addTab("Convert", ConvertTab())
        addTab("Settings", SettingsTab())
    }

    private fun addTab(name: String, component: Component) {
        tabs.addTab(Settings.getLang(name), component)
    }

    fun fillTags(tags: TagReader.TagsData) = tagsTab.fill(tags)
}