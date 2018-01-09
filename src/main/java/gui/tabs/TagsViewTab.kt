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
        viewport.view = table
    }

    private class TagsModel : AbstractTableModel() {
        val columns = arrayOf("File", "Title", "Artist", "Album", "№", "Genre")
                .map { Settings.getLang(it) }
        val data = ArrayList<ArrayList<String?>>()

        init {
            val files = TableModel.changer.getFiles()
            files.parallelStream().forEach { addFile(it) }
        }

        fun addFile(file: File) {
            val list = ArrayList<String?>()
            val tags = tr.readTags(file)
            if (tags.isEmpty())
                return
            list.add(file.name)
            list.add(tags[tr.title])
            list.add(tags[tr.artist])
            list.add(tags[tr.album])
            list.add(tags[tr.track])
            list.add(tags[tr.genre])
            data.add(list)
        }

        override fun getColumnName(col: Int): String = columns[col]

        override fun getColumnCount(): Int = columns.size

        override fun getRowCount(): Int = data.size

        override fun getValueAt(row: Int, col: Int): String = data[row][col] ?: ""

        override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass

    }
}