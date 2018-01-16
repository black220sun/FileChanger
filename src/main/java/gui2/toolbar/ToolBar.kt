package gui2.toolbar

import settings.Settings
import java.awt.Dimension
import javax.swing.JTabbedPane

class ToolBar : JTabbedPane() {
    init {
        preferredSize = Dimension(2000,160)
        minimumSize = preferredSize
        maximumSize = preferredSize
        addTab(Settings.getLang("Files"), FilesTab())
    }
}