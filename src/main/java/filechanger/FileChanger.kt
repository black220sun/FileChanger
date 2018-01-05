package filechanger

import java.io.File
import java.io.FileReader

class FileChanger {
    private val translation = LinkedHashMap<String, String>()
    private val replacement = LinkedHashMap<String, String>()
    private val delimiterCSV = ','
    private val translationPath = "translate.csv"
    private val replacementPath = "replace.csv"

    fun loadReplacement(path: String = replacementPath) {
        load(replacement, path)
    }

    fun loadTranslation(path: String = translationPath) {
        load(translation, path)
    }

    private fun load(provider: LinkedHashMap<String, String>, path: String) {
        val file = File(path)
        if (!file.exists())
            return
        val reader = FileReader(file)
        reader.readLines().filter { it.matches("[^;]+$delimiterCSV[^;]+".toRegex()) }.forEach {
            val entry = it.split(delimiterCSV)
            add(entry[0], entry[1], provider)
        }
        reader.close()
    }

    fun translate(file: File): File {
        return rename(file, translation)
    }

    fun rename(file: File): File {
        return rename(file, replacement)
    }

    private fun rename(file: File, provider: HashMap<String, String>): File {
        var name = file.nameWithoutExtension
        provider.forEach{from, to -> name = name.replace(from, to)}
        val newName = if (file.extension.isEmpty()) name else "$name.${file.extension}"
        val new = File(newName)
        file.renameTo(new)
        return new
    }
    fun move(file: File, path: String): File {
        if (!path.matches("([^/]+)?(/[^/]+)*".toRegex()))
            return file
        File(path).mkdirs()
        val new = File("$path/${file.name}")
        file.renameTo(new)
        return new
    }
    fun moveByName(file: File, path: String = "", from: String = "", to: String = " - "): File {
        if (!path.matches("([^/]+)?(/[^/]+)*".toRegex()))
            return file
        if (from == "" && to == "")
            return file
        val name = file.nameWithoutExtension
        val start = name.indexOf(from) + from.length
        val end = name.indexOf(to)
        val dir = when {
                    to == "" -> name.subSequence(start, name.length)
                    from == "" -> name.subSequence(0, end)
                    else -> name.subSequence(start, end)
                } as String
        val destination = if (path.isEmpty()) dir else "$path/$dir"
        File(destination).mkdirs()
        val new = File("$destination/${file.name}")
        file.renameTo(new)
        return new
    }

    fun addTranslation(from: String, to: String) {
        add(from, to, translation)
    }

    fun addReplacement(from: String, to: String) {
        add(from, to, replacement)
    }

    private fun add(from: String, to: String, provider: HashMap<String, String>) {
        provider.put(from, to)
    }

}