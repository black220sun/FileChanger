package gui.menu

import javax.swing.*

class MenuBar : JMenuBar() {
    init {
        add(FilesMenu())
        add(EditMenu())
        add(MoveMenu())
    }
}