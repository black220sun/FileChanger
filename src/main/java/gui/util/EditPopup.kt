package gui.util

import javax.swing.JPopupMenu
import javax.swing.text.JTextComponent

class EditPopup(comp: JTextComponent) : JPopupMenu (){
    init {
        val cut = LMenuItem("Cut")
        cut.addActionListener { comp.cut() }
        add(cut)

        val cutAll = LMenuItem("Cut all")
        cutAll.addActionListener {
            comp.selectAll()
            comp.cut()
            comp.select(0, 0)
        }
        add(cutAll)

        val copy = LMenuItem("Copy")
        copy.addActionListener { comp.copy() }
        add(copy)

        val copyAll = LMenuItem("Copy all")
        copyAll.addActionListener {
            val start = comp.selectionStart
            val end = comp.selectionEnd
            comp.selectAll()
            comp.copy()
            comp.select(start, end)
        }
        add(copyAll)

        val paste = LMenuItem("Paste")
        paste.addActionListener { comp.paste() }
        add(paste)
    }
}