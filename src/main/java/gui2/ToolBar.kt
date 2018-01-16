package gui2

import gui.util.LButton
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel

class ToolBar : JPanel() {
    init {
        preferredSize = Dimension(1000,100)
        minimumSize = preferredSize
        maximumSize = preferredSize
        layout = BoxLayout(this, BoxLayout.X_AXIS)

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val open = LButton("Open")
        open.addActionListener { MainView.addFiles() }
        panel.add(open)

        val clear = LButton("Clear")
        clear.addActionListener { MainView.clearFiles(false) }
        panel.add(clear)

        val clearSelected = LButton("Clear selected")
        clearSelected.addActionListener { MainView.clearFiles(true) }
        panel.add(clearSelected)

        add(panel)
    }
}