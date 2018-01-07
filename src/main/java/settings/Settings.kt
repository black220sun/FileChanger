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
    private val lang = HashMap<String, String>()
    private val settingsPath = directory + ".settings"
    private lateinit var langPath: String
    private lateinit var langName: String
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
        val fSettings = File(settingsPath)
        val tmpTable = HashMap<String, String>()
        if (!fSettings.exists())
            return
        val reader = FileReader(fSettings)
        reader.readLines()
                .filter { it.matches("[^$csv]+$csv[^$csv]+".toRegex()) }
                .forEach {
                    val parts = it.split(csv)
                    tmpTable.put(parts[0], parts[1])
                }
        reader.close()
        langName = tmpTable.getOrDefault("langName", "English")
        langPath = tmpTable.getOrDefault("langPath", "")
        if (!langPath.isEmpty())
            initLang()
    }

    private fun initLang() {
        val path =
                if (langPath.matches("^(/)|(\\w:\\\\).*".toRegex()))
                    langPath
                else
                    directory + langPath
        val file = File(path)
        if (!file.exists() || !file.isFile)
            return
        val reader = FileReader(file)
        reader.readLines()
                .filter { it.matches("[^$csv]+$csv[^$csv]+".toRegex()) }
                .forEach {
                    val parts = it.split(csv)
                    lang.put(parts[0], parts[1])
                }
        reader.close()
    }

    fun getLangName(): String = langName

    fun getLang(key: String): String = lang.getOrDefault(key, key)
}