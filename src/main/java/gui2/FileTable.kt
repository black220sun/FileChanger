package gui2

import gui.util.EditPopup
import settings.Settings
import settings.Settings.csv
import java.awt.Font
import java.io.FileWriter
import java.io.File
import java.io.FileReader
import javax.swing.DefaultCellEditor
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.TableModel
import javax.swing.text.JTextComponent

class FileTable(model: TableModel) : JTable(model) {
    private val path = Settings.getDirectory() + "order"
    private val tcm = TableColumnManager(this)
    init {
        autoCreateRowSorter = true
        fillsViewportHeight = true
        selectionModel.addListSelectionListener {
            val lsm = it.source as ListSelectionModel
            if (lsm.isSelectionEmpty)
                return@addListSelectionListener
            model as FileModel
            val tags = model.getTags(convertRowIndexToModel(lsm.leadSelectionIndex))
            MainView.fillTags(tags)
        }
        val fontSize = Settings.getSize("sizeFont") ?: 12
        font = Font("Dialog", Font.PLAIN, fontSize)
        val rowSize = Settings.getSize("sizeRow") ?: 14
        setRowHeight(if (rowSize > fontSize) rowSize else fontSize + 1)

        val comp = (getDefaultEditor(Object().javaClass) as DefaultCellEditor).component as JTextComponent
        comp.componentPopupMenu = EditPopup(comp)
    }

    fun save() {
        FileWriter(path).use { writer ->
            columnModel.columns.toList().forEach {
                val index = it.modelIndex
                writer.appendln("$index$csv${it.preferredWidth}$csv${tcm.isHidden(index)}")
            }
        }
    }

    fun load() {
        val file = File(path)
        if (!file.exists())
            return
        val indexes = ArrayList<List<Int>>()
        val hidden = ArrayList<Int>()
        FileReader(file).use {
            it.readLines().filter { it.matches(Regex("\\d+$csv\\d+$csv(true|false)")) }
                    .forEach {
                        val parts = it.split(csv)
                        indexes.add(parts.subList(0, 2).map { it.toInt() })
                        if (parts[2].toBoolean())
                            hidden.add(it[0].toInt())
                    }
        }
        val cols = columnModel.columns.toList()
        while (columnModel.columnCount > 0)
            columnModel.removeColumn(columnModel.getColumn(0))
        indexes.forEach {
            val col = cols[it[0]]
            col.preferredWidth = it[1]
            columnModel.addColumn(col)
        }
    }
}