package gui

import java.io.File
import java.util.*
import javax.swing.table.AbstractTableModel

object TableModel: AbstractTableModel() {
    private val columns = arrayOf("Name", "Type", "Path", "Modified", "Size")
    private val data = ArrayList<ArrayList<Any>>()
    val files = ArrayList<File>()

    override fun getColumnName(col: Int): String = columns[col]

    override fun getColumnCount(): Int = columns.size

    override fun getRowCount(): Int = data.size

    override fun getValueAt(row: Int, col: Int): Any = data[row][col]

    override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass

    fun add(file: File) {
        if (files.contains(file))
            return
        data.add(fileToList(file))
        files.add(file)
        fireTableDataChanged()
    }

    private fun fileToList(file: File): ArrayList<Any> {
        val name = file.name
        val type = if (file.isDirectory) "dir" else file.extension
        val path = file.absoluteFile.parentFile.absolutePath
        val date = Date(file.lastModified())
        var space = file.length()
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
        return arrayListOf(name, type, path, date, total)
    }

    fun clear() {
        files.clear()
        data.clear()
        fireTableDataChanged()
    }
}