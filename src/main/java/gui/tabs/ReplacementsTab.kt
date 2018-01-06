package gui.tabs

import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.AbstractTableModel


class ReplacementsTab(replacements: Collection<Collection<String>>) : JScrollPane() {
    init {
        val table = JTable(ResultModel(replacements))
        viewport.view = table
    }

    private class ResultModel(replacements: Collection<Collection<String>>) : AbstractTableModel() {
        val columns = arrayOf("From", "To", "Type")
        val data = replacements

        override fun getRowCount(): Int = data.size

        override fun getColumnCount(): Int = columns.size

        override fun getValueAt(row: Int, col: Int): Any = data.elementAt(row).elementAt(col)

        override fun getColumnName(col: Int): String = columns[col]
    }
}