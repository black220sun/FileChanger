package gui

import java.awt.Dimension
import javax.swing.*

class FilesTab: JPanel() {
    init {
        val table = JTable(TableModel)
        val scroll = buildScrollPane(table)
        scroll.preferredSize = Dimension(1000,400)
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
        add(scroll)
    }

    private fun buildScrollPane(table: JTable): JScrollPane {
        val scroll = JScrollPane(table)
        scroll.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scroll.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        return  scroll
    }
}