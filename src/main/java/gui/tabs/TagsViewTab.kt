package gui.tabs

import gui.TableModel
import mp3tag.TagReader as tr
import settings.Settings
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.AbstractTableModel
import java.io.File

class TagsViewTab : JScrollPane() {
    init {
        val table = JTable(TagsModel())
        table.autoCreateRowSorter = true
        viewport.view = table
    }

    private class TagsModel : AbstractTableModel() {
        val columns = arrayOf("File", "Title", "Artist", "Album", "№", "Genre")
                .map { Settings.getLang(it) }
        val data = ArrayList<ArrayList<Any?>>()

        init {
            val files = TableModel.changer.getFiles()
            files.parallelStream().forEach { addFile(it) }
        }

        fun addFile(file: File) {
            val list = ArrayList<Any?>()
            val tags = tr.readTags(file)
            if (tags.isEmpty())
                return
            list.add(file.name)
            list.add(tags[tr.title])
            list.add(tags[tr.artist])
            list.add(tags[tr.album])
            val track = tags[tr.track]
            list.add(when {
                track == null -> 0
                track.matches(Regex("\\d*")) -> track.toInt()
                else -> track.filter { it in '0'..'9' }.toInt()
            })
            list.add(tags[tr.genre])
            data.add(list)
        }

        override fun getColumnName(col: Int): String = columns[col]

        override fun getColumnCount(): Int = columns.size

        override fun getRowCount(): Int = data.size

        override fun getValueAt(row: Int, col: Int): Any = data[row][col] ?: ""

        override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass

    }
}