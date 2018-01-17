package gui2

import settings.Settings
import settings.Settings.csv
import java.io.FileWriter
import java.io.File
import java.io.FileReader
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.TableColumn
import javax.swing.table.TableModel

class FileTable(model: TableModel) : JTable(model) {
    private val path = Settings.getDirectory() + "order"
    init {
        autoCreateRowSorter = true
        fillsViewportHeight = true
        val columns = columnModel
        for (i in 0 until columnCount) {
            columns.getColumn(i).preferredWidth = when (i) {
                0 -> 30
                1 -> 350
                2 -> 50
                in 3..5 -> 200
                else -> 70
            }
        }
        selectionModel.addListSelectionListener {
            val lsm = it.source as ListSelectionModel
            if (lsm.isSelectionEmpty)
                return@addListSelectionListener
            model as FileModel
            val tags = model.getTags(convertRowIndexToModel(lsm.leadSelectionIndex))
            MainView.fillTags(tags)
        }
    }

    fun save() {
        FileWriter(path).use { writer ->
            columnModel.columns.toList().forEach {
                writer.appendln("${it.modelIndex}$csv${it.preferredWidth}")
            }
        }
    }

    fun load() {
        val file = File(path)
        if (!file.exists())
            return
        val indexes = ArrayList<List<Int>>()
        FileReader(file).use {
            it.readLines().filter { it.matches(Regex("\\d+$csv\\d+")) }
                    .forEach { indexes.add(it.split(csv).map { it.toInt() }) }
        }
        val cols = columnModel.columns.toList()
        val new = ArrayList<TableColumn>()
        indexes.forEach {
            val col = cols[it[0]]
            col.preferredWidth = it[1]
            new.add(col)
            columnModel.removeColumn(columnModel.getColumn(0))
        }
        new.forEach { columnModel.addColumn(it) }
    }
}