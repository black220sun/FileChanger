package gui2

import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.TableModel

class FileTable(model: TableModel) : JTable(model) {
    init {
        autoCreateRowSorter = true
        fillsViewportHeight = true
        val columns = columnModel
        for (i in 0 until columnCount) {
            columns.getColumn(i).preferredWidth = when (i) {
                0 -> 16
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
}