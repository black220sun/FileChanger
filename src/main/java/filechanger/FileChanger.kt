package filechanger

import java.io.File
import java.io.FileReader

class FileChanger {
    private val translation = LinkedHashMap<String, String>()
    private val replacement = LinkedHashMap<String, String>()
    private val translationPath = "translate.csv"
    private val replacementPath = "replace.csv"

    fun load() {
        init()
        init(replacement, replacementPath)
    }

    fun init(provider: HashMap<String, String> = translation, path: String = translationPath) {
        val file = File(path)
        if (!file.exists())
            return
        val reader = FileReader(file)
        reader.readLines().filter { it.matches("[^;]+;[^;]+".toRegex()) }.forEach {
            val entry = it.split(';')
            add(entry[0], entry[1], provider)
        }
        reader.close()
    }

    fun translate(file: File): File {
        return rename(file, translation)
    }

    fun rename(file: File, provider: HashMap<String, String> = replacement): File {
        var name = file.nameWithoutExtension
        provider.forEach{from, to -> name = name.replace(from, to)}
        val newName = if (file.extension.isEmpty()) name else "$name.${file.extension}"
        val new = File(newName)
        file.renameTo(new)
        return new
    }
    fun move(file: File, path: String): File {
        if (!path.matches("(/[^\\s/]+)*".toRegex()))
            return file
        File(path).mkdirs()
        val new = File("$path/${file.name}")
        file.renameTo(new)
        return new
    }
    fun moveByName(file: File, path: String = "", delimiter: String = "_-_"): File {
        if (!path.matches("(/[^\\s/]+)*".toRegex()))
            return file
        val dir = file.name.split(delimiter)[0]
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

    fun add(from: String, to: String, provider: HashMap<String, String> = replacement) {
        provider.put(from, to)
    }

}