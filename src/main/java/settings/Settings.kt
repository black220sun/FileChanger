package settings

import java.io.*
import javax.swing.JOptionPane

object Settings {
    val separator = System.getProperty("file.separator")!!
    val csv = ","
    val home = System.getProperty("user.home")!!
    private val directory = home + separator + ".FileChanger" + separator
    private val properties = HashMap<String, String>()
    private val settingsPath = directory + "settings"
    private val lang = Language()
    private val force = Force()
    private val saveLoad = SaveLoad()
    val resources = directory + "res" + separator
    init {
        val dir = File(directory)
        if (dir.isFile) {
            JOptionPane.showMessageDialog(null, "Can`t init settings. Working directory is a file",
                    "Error", JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }
        if (!dir.exists())
            dir.mkdirs()
        init()
    }

    private fun init() {
        loadFile(settingsPath, properties)
        val langAvalPath = properties["languages"]
        if (langAvalPath != null)
            lang.initLang(langAvalPath)
    }

    fun loadFile(filePath: String, table: HashMap<String, String>) {
        val path =
                if (filePath.matches("^(/|[A-H]:\\\\).*".toRegex()))
                    filePath
                else
                    directory + filePath
        val file = File(path)
        if (!file.exists() || !file.isFile)
            return
//        val reader = FileReader(file)
        val reader = InputStreamReader(file.inputStream(), lang.defaultCharset)
        reader.readLines()
                .filter { it.matches("[^$csv]+$csv[^$csv]+".toRegex()) }
                .forEach {
                    val parts = it.split(csv)
                    table.put(parts[0], parts[1])
                }
        reader.close()
    }

    fun saveFile(filePath: String, table: HashMap<String, String>) {
        val path =
                if (filePath.matches("^(/|[A-H]:\\\\).*".toRegex()))
                    filePath
                else
                    directory + filePath
        OutputStreamWriter(FileOutputStream(path), lang.defaultCharset).use {
            table.forEach { key, value -> it.appendln("$key$csv$value") }
        }
    }

    fun save() {
        saveFile(settingsPath, properties)
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
    fun showHidden(): Boolean = properties.getOrPut("showHidden", {"false"}).toBoolean()
}