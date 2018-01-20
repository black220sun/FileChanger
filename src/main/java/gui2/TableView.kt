package gui2

import java.awt.Dimension
import javax.swing.JScrollPane
import javax.swing.JTable

class TableView(table: JTable) : JScrollPane(table) {
    init {
        minimumSize = Dimension(600, 400)
    }
}