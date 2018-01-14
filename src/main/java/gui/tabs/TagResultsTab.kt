package gui.tabs

import settings.Settings
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.AbstractTableModel
import java.io.File
import mp3tag.TagReader as tr


class TagResultsTab(files: List<List<Any>>) : JScrollPane() {
    init {
        val table = JTable(ResultModel(files))
        table.autoCreateRowSorter = true
        viewport.view = table
        val columns = table.columnModel
        for (i in 0 until table.columnCount) {
            columns.getColumn(i).preferredWidth = when (i) {
                0 -> 250
                1 -> 350
                in 2..4 -> 250
                else -> 70
            }
        }
    }

    private class ResultModel(result: List<List<Any>>) : AbstractTableModel() {
        val columns = arrayOf("Name", "Path", "Title", "Artist", "Album", "Year", "№", "Genre")
                .map { Settings.getLang(it) }
        val data = createData(result)

        @Suppress("UNCHECKED_CAST")
        private fun createData(result: List<List<Any>>): List<List<Any?>> {
            val data = ArrayList<ArrayList<Any?>>()
            val olds = result[0] as List<File>
            val news = result[1] as List<HashMap<String, String>>
            for (i in 0 until olds.size) {
                val old = olds[i].absoluteFile
                val new = news[i]
                val year = new[tr.year]
                val intYear = when {
                    year == null -> 0
                    year == "" -> 0
                    year.matches(Regex("\\d*")) -> year.toInt()
                    else -> (year.subSequence(0, year.indexOfFirst { it !in '0'..'9' }) as String).toInt()
                }
                val track = new[tr.track]
                val intTrack = when {
                    track == null -> 0
                    track == "" -> 0
                    track.matches(Regex("\\d*")) -> track.toInt()
                    else -> (track.subSequence(0, track.indexOfFirst { it !in '0'..'9' }) as String).toInt()
                }
                data.add(arrayListOf(old.name, old.parentFile.absolutePath,
                        new[tr.title], new[tr.artist], new[tr.album], intYear,
                        intTrack, new[tr.genre]))
            }
            return data
        }

        override fun getRowCount(): Int = data.size

        override fun getColumnCount(): Int = columns.size

        override fun getValueAt(row: Int, col: Int): Any = data[row][col] ?: ""

        override fun getColumnName(col: Int): String = columns[col]

        override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass
    }
}