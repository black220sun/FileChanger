package gui.util

import settings.Settings
import java.io.*
import javax.swing.JComboBox

class LPattern : JComboBox<String>() {
    private val path = Settings.getDirectory() + "patterns"
    init {
        val file = File(path)
        if (file.exists())
            InputStreamReader(file.inputStream(), Settings.defaultCharset).use {
                it.forEachLine { addItem(it) }
            }
    }

    fun save() {
        OutputStreamWriter(FileOutputStream(path), Settings.defaultCharset).use { writer ->
            (0 until itemCount).forEach {
                writer.appendln(getItemAt(it))
            }
        }
    }

    fun addText(text: String) {
        (0 until itemCount).forEach {
            if (text == getItemAt(it))
                return
        }
        addItem(text)
        selectedIndex = itemCount - 1
        save()
    }

    fun removeText() {
        removeItemAt(selectedIndex)
        save()
    }
}