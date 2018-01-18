package gui2

import gui.util.LMenuItem
import settings.Settings
import settings.Settings.csv
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.FileWriter
import java.io.File
import java.io.FileReader
import javax.swing.JPopupMenu
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.TableModel

class FileTable(model: TableModel) : JTable(model), MouseListener {
    override fun mouseReleased(p0: MouseEvent?) = Unit

    override fun mouseEntered(p0: MouseEvent) = Unit

    override fun mouseClicked(p0: MouseEvent) = Unit

    override fun mouseExited(p0: MouseEvent) = Unit

    override fun mousePressed(e: MouseEvent) {
        if (e.button != MouseEvent.BUTTON3)
            return
        val point = e.point
        val col = columnAtPoint(point)
        if (col > columnCount)
            return
        val row = rowAtPoint(point)
        if (row > rowCount)
            return
        val edit = isCellEditable(row, col)
        val popUp = JPopupMenu()

        val cut = LMenuItem("Cut")
        cut.isEnabled = edit
        popUp.add(cut)

        val copy = LMenuItem("Copy")
        popUp.add(copy)

        val paste = LMenuItem("Paste")
        paste.isEnabled = edit
        popUp.add(paste)

        popUp.show(this, point.x, point.y)
    }

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
        addMouseListener(this)
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