package filechanger

import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileChangerContainer {
    enum class Type {
        REPLACEMENT, TRANSLATION, REGEX
    }
    private val changer = FileChanger()
    private var files = ArrayList<File>()

    fun getFiles(): List<File> = files

    fun init() {
        changer.loadReplacement()
        changer.loadTranslation()
        changer.loadRegex()
    }

    fun load(path: String, table: Type = Type.REPLACEMENT) {
        when (table) {
            Type.REPLACEMENT -> changer.loadReplacement(path)
            Type.TRANSLATION -> changer.loadTranslation(path)
            Type.REGEX -> changer.loadRegex(path)
        }
    }

    fun add(from: String, to: String, table: Type = Type.REPLACEMENT) {
        when (table) {
            Type.REPLACEMENT -> changer.addReplacement(from, to)
            Type.TRANSLATION -> changer.addTranslation(from, to)
            Type.REGEX -> changer.addRegex(from, to)
        }
    }

    private fun exec(filter: (File)->Boolean = {true}, action: (File)->File): List<File> {
        files = files.map { if (filter(it)) action(it) else it } as ArrayList<File>
        return files
    }

    fun rename(filter: (File)->Boolean = {true}, regex: Boolean = false): List<File> =
            exec(filter, { changer.rename(it, regex) })

    fun translate(filter: (File)->Boolean = {true}): List<File> =
            exec(filter, { changer.translate(it) })

    fun move(path: String, filter: (File) -> Boolean = {true}): List<File> =
            exec(filter, { changer.move(it, path) })

    fun move(dir: File, filter: (File) -> Boolean = {true}): List<File> =
            exec(filter, { changer.move(it, dir) })

    fun moveByName(path: String = "", filter: (File) -> Boolean = {true}, from: String = "", to: String = " - "): List<File> =
            exec(filter, { changer.moveByName(it, path, from, to) })

    fun saveFiles(path: String) {
        val file = File(path)
        val dir = file.absoluteFile.parentFile
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