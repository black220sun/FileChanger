package filechanger

import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileChangerContainer {
    enum class LoadOptions {
        REPLACEMENT, TRANSLATION, REGEX
    }
    private val changer = FileChanger()
    private val files = ArrayList<File>()

    fun init() {
        changer.loadReplacement()
        changer.loadTranslation()
        changer.loadRegex()
    }

    fun load(path: String, table: LoadOptions = LoadOptions.REPLACEMENT) {
        when (table) {
            LoadOptions.REPLACEMENT -> changer.loadReplacement(path)
            LoadOptions.TRANSLATION -> changer.loadTranslation(path)
            LoadOptions.REGEX -> changer.loadRegex(path)
        }
    }

    private fun exec(filter: (File)->Boolean = {true}, action: (File)->File): List<File> {
        val new = ArrayList<File>()
        files.filter(filter).forEach { new.add(action(it)) }
        files.removeIf(filter)
        new.forEach { files.add(it) }
        return files
    }

    fun rename(filter: (File)->Boolean = {true}, regex: Boolean = false): List<File> =
        exec(filter, { changer.rename(it, regex) })

    fun translate(filter: (File)->Boolean = {true}): List<File> =
        exec(filter, { changer.translate(it) })

    fun move(path: String, filter: (File) -> Boolean = {true}): List<File> =
        exec(filter, { changer.move(it, path) })

    fun moveByName(path: String = "", filter: (File) -> Boolean = {true}, from: String = "", to: String = " - "): List<File> =
        exec(filter, { changer.moveByName(it, path, from, to) })

    fun saveFiles(path: String) {
        val file = File(path)
        val dir = file.parentFile
        if (!dir.exists())
            dir.mkdirs()
        val writer = FileWriter(file)
        files.forEach { writer.appendln(it.absolutePath) }
        writer.close()
    }

    fun loadFiles(path: String): List<File> {
        val file = File(path)
        if (file.exists()) {
            val reader = FileReader(file)
            reader.forEachLine { files.add(File(it)) }
            files.removeIf { !it.exists() }
        }
        return files
    }

    fun addFile(file: File): List<File> {
        if (file.exists())
            files.add(file)
        return files
    }

    fun addFile(path: String): List<File> {
        addFile(File(path))
        return files
    }

    fun addFiles(file: Collection<File>): List<File> {
        file.forEach { addFile(it) }
        return files
    }

    fun removeFile(file: File): List<File> {
        files.remove(file)
        return files
    }

    fun removeFiles(condition: (File)->Boolean): List<File> {
        files.removeIf(condition)
        return files
    }
}