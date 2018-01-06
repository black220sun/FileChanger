package filechanger

import java.io.File
import java.io.FileReader

class FileChanger {
    private val translation = LinkedHashMap<String, String>()
    private val replacement = LinkedHashMap<String, String>()
    private val replacementRegex = LinkedHashMap<String, String>()
    private val delimiterCSV = ','
    private val translationPath = "translate.csv"
    private val replacementPath = "replace.csv"
    private val replacementRegexPath = "replace_regex.csv"
    private val pathRegex = "([^/]+)?(/[^/]+)*".toRegex()

    fun loadReplacement(path: String = replacementPath) {
        load(replacement, path)
    }

    fun loadTranslation(path: String = translationPath) {
        load(translation, path)
    }

    fun loadRegex(path: String = replacementRegexPath) {
        load(replacementRegex, path)
    }

    fun clearReplacement() {
        clear(replacement)
    }

    fun clearTranslation() {
        clear(translation)
    }

    fun clearRegex() {
        clear(replacementRegex)
    }

    private fun clear(provider: LinkedHashMap<String, String>) {
        provider.clear()
    }

    private fun load(provider: LinkedHashMap<String, String>, path: String) {
        val file = File(path)
        if (!file.exists())
            return
        val reader = FileReader(file)
        reader.readLines().filter { it.matches("[^$delimiterCSV]+$delimiterCSV[^$delimiterCSV]*".toRegex()) }.forEach {
            val entry = it.split(delimiterCSV)
            add(entry[0], entry[1], provider)
        }
        reader.close()
    }

    fun translate(file: File, force: Boolean = true): File {
        return rename(file, translation, false, force)
    }

    fun rename(file: File, regex: Boolean = false, force: Boolean = true): File {
        return rename(file, if (regex) replacementRegex else replacement, regex, force)
    }

    private fun rename(file: File, provider: HashMap<String, String>, regex: Boolean, force: Boolean): File {
        val dir = file.absoluteFile.parentFile.absolutePath
        var name = file.nameWithoutExtension
        provider.forEach{from, to -> name = if (regex) name.replace(from.toRegex(), to) else name.replace(from, to)}
        val newName = if (file.extension.isEmpty()) name else "$name.${file.extension}"
        val new = File("$dir/$newName")
        if (force)
            file.renameTo(new)
        return new
    }

    fun move(file: File, path: String, force: Boolean = true): File {
        if (!path.matches(pathRegex))
            return file
        File(path).mkdirs()
        val new = File("$path/${file.name}")
        if (force)
            file.renameTo(new)
        return new
    }

    fun move(file: File, dir: File, force: Boolean = true): File {
        if (!dir.isDirectory)
            return file
        if (!dir.exists())
            dir.mkdirs()
        val new = File("${dir.absolutePath}/${file.name}")
        if (force)
            file.renameTo(new)
        return new
    }

    fun moveByName(file: File, path: String = "", from: String = "", to: String = " - ", force: Boolean = true): File {
        if (!path.matches(pathRegex))
            return file
        if (from == "" && to == "")
            return file
        val name = file.nameWithoutExtension
        var start = name.indexOf(from)
        if (start != -1)
            start += from.length
        val end = name.indexOf(to)
        if (start == -1 && end == -1)
            return file
        val dir = when {
                    to == "" -> name.subSequence(start, name.length)
                    from == "" -> name.subSequence(0, end)
                    else -> name.subSequence(start, end)
                } as String
        val destination = if (path.isEmpty()) dir else "$path/$dir"
        File(destination).mkdirs()
        val new = File("$destination/${file.name}")
        if (force)
            file.renameTo(new)
        return new
    }

    fun addTranslation(from: String, to: String) {
        add(from, to, translation)
    }

    fun addReplacement(from: String, to: String) {
        add(from, to, replacement)
    }

    fun addRegex(from: String, to: String) {
        add(from, to, replacementRegex)
    }

    private fun add(from: String, to: String, provider: LinkedHashMap<String, String>) {
        provider.put(from, to)
    }

    fun getReplacements(): ArrayList<ArrayList<String>> {
        val out = ArrayList<ArrayList<String>>()
        translation.forEach { from, to ->  out.add(arrayListOf(from, to, "translation")) }
        replacement.forEach { from, to ->  out.add(arrayListOf(from, to, "replacement")) }
        replacementRegex.forEach { from, to ->  out.add(arrayListOf(from, to, "regex")) }
        return out
    }

}