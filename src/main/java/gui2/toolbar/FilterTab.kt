package gui2.toolbar

import gui.util.LIcon
import gui2.MainView
import settings.Settings
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.AbstractTableModel

class FilterTab : JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        val modelName = TableModel("Name", false)
        val modelExt = TableModel("Extension", true)
        val tableName = JTable(modelName)
        val tableExt = JTable(modelExt)
        val scrollName = JScrollPane(tableName)
        val panelName = JPanel()
        panelName.layout = BoxLayout(panelName, BoxLayout.Y_AXIS)
        val scrollExt = JScrollPane(tableExt)
        val panelExt = JPanel()
        panelExt.layout = BoxLayout(panelExt, BoxLayout.Y_AXIS)
        add(scrollName)
        add(panelName)
        add(scrollExt)
        add(panelExt)

        run {
            val add = LIcon("add.png")
            add.addActionListener { modelName.add() }
            panelName.add(add)

            val delete = LIcon("delete.png")
            delete.addActionListener {
                modelName.delete(tableName.selectedRows.map { tableName.convertRowIndexToModel(it) })
            }
            panelName.add(delete)

            val process = LIcon("process.png")
            process.addActionListener { MainView.select(modelName.get(), false) }
            panelName.add(process)
        }
        run {
            val add = LIcon("add.png")
            add.addActionListener { modelExt.add() }
            panelExt.add(add)

            val delete = LIcon("delete.png")
            delete.addActionListener {
                modelExt.delete(tableExt.selectedRows.map { tableExt.convertRowIndexToModel(it) })
            }
            panelExt.add(delete)

            val process = LIcon("process.png")
            process.addActionListener { MainView.select(modelExt.get(), true) }
            panelExt.add(process)
        }
    }

    private class TableModel(name: String, val save: Boolean) : AbstractTableModel() {
        private val columns = arrayOf("", Settings.getLang(name))
        private val data = ArrayList<ArrayList<Any?>>()
        private val path = Settings.getDirectory() + name.toLowerCase()
        private val csv = Settings.csv

        init {
            val file = File(path)
            if (file.exists()) {
                InputStreamReader(file.inputStream(), Settings.defaultCharset).use {
                    it.readLines()
                            .filter { it.matches(Regex("[^$csv]+$csv[^$csv]+")) }
                            .forEach {
                                val tmp = it.split(csv)
                                data.add(arrayListOf(tmp[0].toBoolean(), tmp[1]))
                            }
                }
                fireTableDataChanged()
            }
        }

        override fun isCellEditable(p0: Int, p1: Int): Boolean = true
        override fun getRowCount(): Int = data.size
        override fun getColumnName(col: Int): String = columns[col]
        override fun setValueAt(value: Any, row: Int, col: Int) {
            data[row][col] = value
            if (save) save()
        }
        override fun getColumnCount(): Int = columns.size
        override fun getColumnClass(col: Int): Class<*> = getValueAt(0, col).javaClass

        override fun getValueAt(row: Int, col: Int): Any = data[row][col] ?: ""

        private fun save() {
            OutputStreamWriter(FileOutputStream(path), Settings.defaultCharset).use { writer ->
                data.forEach {
                    writer.appendln("${it[0]}$csv${it[1]}")
                }
            }
        }

        fun add() {
            data.add(arrayListOf(false,""))
            fireTableDataChanged()
        }

        fun delete(rows: List<Int>) {
            rows.sortedDescending().forEach { data.removeAt(it) }
            fireTableDataChanged()
            if (save) save()
        }

        fun get(): List<String> = data.filter { it[0] == true }.map { it[1] as String }
    }
}