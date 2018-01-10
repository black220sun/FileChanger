package gui.tabs

import gui.TableModel
import javax.swing.*

class FilesTab: JScrollPane() {
    init {
        val table = JTable(TableModel)
        table.autoCreateRowSorter = true
        horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        viewport.view = table
        table.fillsViewportHeight = true
        val columns = table.columnModel
        for (i in 0 until table.columnCount) {
            columns.getColumn(i).preferredWidth = when (i) {
                0 -> 350
                1 -> 30
                2 -> 300
                else -> 70
            }
        }
    }
}