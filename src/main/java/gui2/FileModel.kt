package gui2

import mp3tag.TagReader as tr
import settings.Settings
import javax.swing.table.AbstractTableModel
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileModel : AbstractTableModel() {
    private val startTags: Int = 5
    private val columns = arrayOf("", "File", "Type", "Size", "Tags", "Title", "Artist", "Album", "Year", "№", "Genre").map { Settings.getLang(it) }
    private val data = ArrayList<ArrayList<Any?>>()

    fun addDir(file: File) {
        if (!file.isDirectory)
            return
        file.listFiles().forEach { addFile(it) }
        fireTableDataChanged()
    }

    fun showDir(dir: File) {
        data.clear()
        addDir(dir)
    }

    fun clear(selected: Boolean = false) {
        if (selected)
            data.removeIf { !(it[0] as Boolean) }
         else
            data.clear()
        fireTableDataChanged()
    }


    fun selectAll(state: Boolean) {
        data.forEach { it[0] = state }
        fireTableDataChanged()
    }

    private fun addFile(file: File, selected: Boolean = false) {
        val row = ArrayList<Any?>()
        row.add(selected)
        row.add(file.name)
        row.add(if (file.isDirectory) "dir" else file.extension)
        val size = file.length()
        row.add(size)
        val tags = tr.readTags(file)
        row.add(tags.tagSize())
        row.add(tags.title())
        row.add(tags.artist())
        row.add(tags.album())
        row.add(tags.year())
        row.add(tags.track())
        row.add(tags.genre())
        row.add(tags)
        row.add(file)
        data.add(row)
    }


    fun saveFiles(path: String) {
        val file = File(path)
        val dir = file.absoluteFile.parentFile
        if (!dir.exists())
            dir.mkdirs()
        FileWriter(file).use { writer -> data.forEach { writer.appendln((it.last() as File).absolutePath) } }
    }

    fun loadFiles(path: String) {
        val file = File(path)
        if (file.exists()) {
            FileReader(file).use {
                it.forEachLine {
                    val new = File(it)
                    if (new.exists())
                        addFile(new)
                }
            }
        }
        fireTableDataChanged()
    }

    fun getTags(row: Int): mp3tag.TagReader.TagsData = data[row][data[row].lastIndex - 1] as mp3tag.TagReader.TagsData
    override fun getRowCount(): Int = data.size
    override fun getColumnCount(): Int = columns.size
    override fun getValueAt(row: Int, col: Int): Any = try { data[row][col] ?: "" } catch (e: Exception) { "" }
    override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass
    override fun getColumnName(col: Int): String = columns[col]
    override fun isCellEditable(row: Int, col: Int): Boolean = col == 0 || data[row][0] == true
    override fun setValueAt(value: Any?, row: Int, col: Int) {
        data[row][col] = value
        fireTableCellUpdated(row, col)
    }

    fun select(filter: List<String>, ext: Boolean) {
        val regex = filter.map { Regex(it) }
        if (ext)
            selectExt(regex)
        else
            selectName(regex)
        fireTableDataChanged()
    }

    private fun selectName(regex: List<Regex>) {
        data.forEach { row ->
            var flag = true
            regex.forEach {
                @Suppress("LABEL_NAME_CLASH")
                if ((row.last() as File).nameWithoutExtension.matches(it)) {
                    row[0] = true
                    flag = false
                    return@forEach
                }
            }
            if (flag)
                row[0] = false
        }
    }

    private fun selectExt(regex: List<Regex>) {
        data.forEach { row ->
            var flag = true
            regex.forEach {
                @Suppress("LABEL_NAME_CLASH")
                if ((row[2] as String).matches(it)) {
                    row[0] = true
                    flag = false
                    return@forEach
                }
            }
            if (flag)
                row[0] = false
        }
    }

    fun save() {
        data.filter { it[0] == true }
                .forEach {
                    val tags = it[it.lastIndex - 1] as tr.TagsData
                    val lst = tags.toList()
                    val real = it.subList(startTags, it.lastIndex - 1)
                    val size = real.size
                    var flag = false
                    (0 until size).forEach {
                        if (real[it] != lst[it]) {
                            tags.index(it, real[it] ?: "")
                            flag = true
                        }
                    }
                    val file = it.last() as File
                    if (flag)
                        tr.writeTags(file, tags)
                    val new = it[1] as String
                    var newFile = file
                    if (new != file.name) {
                        val tmp = File(file.absoluteFile.parentFile.absolutePath + File.separator + new)
                        if (file.renameTo(tmp)) {
                            newFile = tmp
                            flag = true
                        }
                    }
                    if (flag) {
                        data.removeIf { it.last() == file }
                        addFile(newFile, true)
                    }
                }
        fireTableDataChanged()
    }

    fun invert() {
        data.forEach { it[0] = !(it[0] as Boolean) }
        fireTableDataChanged()
    }
}