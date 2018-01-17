package gui2.toolbar

import mp3tag.TagReader
import settings.Settings
import java.awt.Component
import java.awt.Dimension
import javax.swing.JTabbedPane

class ToolBar : JTabbedPane() {
    private val tagsTab = TagsTab()
    init {
        preferredSize = Dimension(2000,160)
        minimumSize = preferredSize
        maximumSize = preferredSize

        addTab("Files", FilesTab())
        addTab("Filter", FilterTab())
        addTab("Replace", ReplaceTab(false))
        addTab("Translate", ReplaceTab(true))
        addTab("Tags", tagsTab)
        addTab("Convert", ConvertTab())
        addTab("Settings", SettingsTab())
    }

    override fun addTab(name: String, component: Component) {
        super.addTab(Settings.getLang(name), component)
    }

    fun fillTags(tags: TagReader.TagsData) = tagsTab.fill(tags)
}