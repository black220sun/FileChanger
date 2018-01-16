package gui2

import java.awt.Dimension
import javax.swing.JScrollPane
import javax.swing.JTable

class TableView(table: JTable) : JScrollPane(table) {
    init {
        maximumSize = Dimension(2000, 1000)
    }
}