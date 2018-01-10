package gui.tabs

import settings.Settings
import java.io.File
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.AbstractTableModel


class ResultsTab(files: Collection<Collection<File>>) : JScrollPane() {
    init {
        val table = JTable(ResultModel(files))
        table.autoCreateRowSorter = true
        viewport.view = table
    }

    private class ResultModel(files: Collection<Collection<File>>) : AbstractTableModel() {
        val columns = arrayOf("Old name", "Old path", "New name", "New path")
                .map { Settings.getLang(it) }
        val data = createData(files)

        private fun createData(files: Collection<Collection<File>>): Collection<Collection<String>> {
            val data = ArrayList<ArrayList<String>>()
            val olds = files.elementAt(0).toTypedArray()
            val news = files.elementAt(1).toTypedArray()
            for (i in 0 until olds.size) {
                val old = olds[i].absoluteFile
                val new = news[i].absoluteFile
                data.add(arrayListOf(old.name, old.parentFile.absolutePath,
                        new.name, new.parentFile.absolutePath))
            }
            return data
        }

        override fun getRowCount(): Int = data.size

        override fun getColumnCount(): Int = columns.size

        override fun getValueAt(row: Int, col: Int): Any = data.elementAt(row).elementAt(col)

        override fun getColumnName(col: Int): String = columns[col]
    }
}