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
    private val settingsPath = directory + "settings"
    private val lang = Language()
    private val force = Force()
    private val saveLoad = SaveLoad()
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
            lang.initLang(langAvalPath)
    }

    fun initFile(filePath: String, table: HashMap<String, String>) {
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

    fun getDirectory(): String = directory

    //TODO(implement check for key change availability)
    private fun check(key: String) : Boolean = true

    fun getLang(key: String): String = lang.getLang(key)
    fun getLanguages(): Array<String> = lang.getLanguages()
    fun getLangName(): String = lang.getLangName()
    fun setLangName(value: String) = lang.setLangName(value)
    fun saveLang() = lang.saveLang()
    fun getForce(key: String): Boolean = force.getMode(key)
    fun setForce(key: String, value: Boolean) = force.setMode(key, value)
    fun getSaveLoad(): Boolean = saveLoad.getSaveLoad()
    fun setSaveLoad(state: Boolean) = saveLoad.setSaveLoad(state)
    fun getSaveLoadPath(): String = saveLoad.getPath()
}