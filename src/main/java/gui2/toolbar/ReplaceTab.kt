package gui2.toolbar

import gui.util.LCheckBox
import gui.util.LIcon
import gui.util.LLabel
import settings.Settings
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*
import javax.swing.table.AbstractTableModel

class ReplaceTab(translate: Boolean) : JPanel() {
    init {
        layout = BorderLayout()
        val model = TableModel()
        val table = JTable(model)
        val scroll = JScrollPane(table)
        add(BorderLayout.CENTER, scroll)
        val panel = JPanel()
        panel.layout = GridLayout(3, 2)
        add(BorderLayout.EAST, panel)

        val add = LIcon("add.png")
        add.addActionListener { model.add() }
        panel.add(add)

        val delete = LIcon("delete.png")
        delete.addActionListener { model.delete(table.selectedRows.map { table.convertRowIndexToModel(it) }) }
        panel.add(delete)

        val save = LIcon("save.png")
        save.addActionListener { /*TODO(implement)*/ }
        panel.add(save)

        val load = LIcon("load.png")
        load.addActionListener { /*TODO(implement)*/ }
        panel.add(load)


        val regex = LCheckBox("Regex")
        if (translate)
            panel.add(LLabel(""))
        else
            panel.add(regex)

        val process = LIcon("process.png")
        process.addActionListener { /*TODO(implement)*/ }
        panel.add(process)
    }

    private class TableModel : AbstractTableModel() {
        private val columns = arrayOf("From", "To").map { Settings.getLang(it) }
        private val data = ArrayList<ArrayList<String>>()

        override fun isCellEditable(p0: Int, p1: Int): Boolean = true
        override fun getRowCount(): Int = data.size
        override fun getColumnName(col: Int): String = columns[col]
        override fun setValueAt(value: Any, row: Int, col: Int) {
            data[row][col] = value as String
        }
        override fun getColumnCount(): Int = columns.size
        override fun getValueAt(row: Int, col: Int): Any = data[row][col]
        fun add() {
            data.add(arrayListOf("", ""))
            fireTableDataChanged()
        }
        fun delete(rows: List<Int>) {
            rows.sortedDescending().forEach { data.removeAt(it) }
            fireTableDataChanged()
        }
    }
}