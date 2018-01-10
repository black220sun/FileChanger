package gui

import filechanger.FileChangerContainer
import settings.Settings
import java.io.File
import java.util.*
import javax.swing.table.AbstractTableModel

object TableModel: AbstractTableModel() {
    private val columns = arrayOf("Name", "Type", "Path", "Modified", "Size")
            .map { Settings.getLang(it) }
    private val data = ArrayList<ArrayList<Any>>()
    val changer = FileChangerContainer()

    override fun getColumnName(col: Int): String = columns[col]

    override fun getColumnCount(): Int = columns.size

    override fun getRowCount(): Int = data.size

    override fun getValueAt(row: Int, col: Int): Any {
        return when (col) {
            in 0..2 -> data[row][col] as String
            3 -> Date(data[row][col] as Long)
            else -> data[row][col] as Long
        }
    }

    override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass

    private fun sizeToString(size: Long): String {
        var space = size
        var total = ""
        val kb = 1024
        val mb = kb * kb
        val gb = mb * kb
        var measure  = "b"
        if ((space / gb) != 0L) {
            total += (space / gb).toString() + " "
            space %= gb
            measure = "Gb"
        }
        if ((space / mb) != 0L) {
            total += (space / mb).toString() + " "
            space %= mb
            measure = "Mb"
        }
        if ((space / kb) != 0L) {
            total += (space / kb).toString() + " "
            space %= kb
            measure = "Kb"
        }
        if (space != 0L) {
            total += space.toString()
            measure = "b"
        }
        total += measure
        return total
    }

    fun add(file: File) {
        if (changer.getFiles().contains(file))
            return
        changer.addFile(file)
        data.add(fileToList(file))
        fireTableDataChanged()
    }

    private fun fileToList(file: File): ArrayList<Any> {
        val name = file.name
        val type = if (file.isDirectory) "dir" else file.extension
        val path = file.absoluteFile.parentFile.absolutePath
        val date = file.lastModified()
        val space = file.length()
        return arrayListOf(name, type, path, date, space)
    }

    fun clear() {
        changer.removeFiles()
        data.clear()
        fireTableDataChanged()
    }

    fun update() {
        data.clear()
        changer.getFiles().forEach { data.add(fileToList(it)) }
        fireTableDataChanged()
    }
}