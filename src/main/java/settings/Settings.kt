package settings

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.swing.JOptionPane

object Settings {
    val separator = System.getProperty("file.separator")!!
    val csv = ","
    private val home = System.getProperty("user.home")!!
    private val directory = home + separator + ".FileChanger" + separator
    private val properties = HashMap<String, String>()
    private val lang = HashMap<String, String>()
    private val settingsPath = directory + "settings"
    val languages = HashMap<String, String>()
    private val defaultLang = "English"
    init {
        val dir = File(directory)
        if (dir.isFile) {
            JOptionPane.showMessageDialog(null, "Can`t init settings. Working directory is a file",
                    "Error", JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }
        if (!dir.exists()) {
            dir.mkdirs()
        } else
            init()
    }

    private fun init() {
        initFile(settingsPath, properties)
        val langAvalPath = properties["languages"]
        if (langAvalPath != null)
            initLang(langAvalPath)
    }

    private fun initLang(langPath: String) {
        initFile(langPath, languages)
        val lang = properties["langName"]
        if (lang == null) {
            properties.put("langName", defaultLang)
            properties.put("langActive", defaultLang)
            return
        }
        properties.put("langActive", lang)
        val path = languages[lang] ?: return
        initFile(path, this.lang)
    }

    private fun initFile(filePath: String, table: HashMap<String, String>) {
        val path =
                if (filePath.matches("^(/|\\w:\\\\).*".toRegex()))
                    filePath
                else
                    directory + filePath
        val file = File(path)
        if (!file.exists() || !file.isFile)
            return
        val reader = FileReader(file)
        reader.readLines()
                .filter { it.matches("[^$csv]+$csv[^$csv]+".toRegex()) }
                .forEach {
                    val parts = it.split(csv)
                    table.put(parts[0], parts[1])
                }
        reader.close()
    }

    fun getLang(key: String): String {
        return if (lang.containsKey(key))
            lang[key]!!
        else {
            lang.put(key, key)
            key
        }
    }

    fun saveLang() {
        val langName = properties["langActive"]
        val filePath = languages[langName] ?: return
        val path =
                if (filePath.matches("^(/|\\w:\\\\).*".toRegex()))
                    filePath
                else
                    directory + filePath
        val writer = FileWriter(File(path))
        lang.forEach { key, value -> writer.appendln("$key$csv$value") }
        writer.close()
    }

    fun save() {
        val writer = FileWriter(File(settingsPath))
        properties.forEach { key, value ->  writer.appendln("$key$csv$value")}
        writer.close()
    }

    fun getProperty(key: String): String? = properties[key]

    fun setProperty(key: String, value: String) {
        if (check(key))
            properties.put(key, value)
    }
    //TODO(implement check for key change availability)
    private fun check(key: String) : Boolean = true
}