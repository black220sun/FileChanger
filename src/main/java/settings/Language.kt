package settings

import java.io.File
import java.io.FileWriter

class Language {
    private val lang = HashMap<String, String>()
    private val languages = HashMap<String, String>()
    private val defaultLang = "English"

    fun initLang(langPath: String) {
        Settings.initFile(langPath, languages)
        val lang = Settings.getProperty("langName")
        if (lang == null) {
            Settings.setProperty("langName", defaultLang)
            Settings.setProperty("langActive", defaultLang)
            return
        }
        Settings.setProperty("langActive", lang)
        val path = languages[lang] ?: return
        Settings.initFile(path, this.lang)
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
        val langName = Settings.getProperty("langActive")
        val filePath = languages[langName] ?: return
        val path =
                if (filePath.matches("^(/|\\w:\\\\).*".toRegex()))
                    filePath
                else
                    Settings.getDirectory() + filePath
        val writer = FileWriter(File(path))
        lang.forEach { key, value -> writer.appendln("$key${Settings.csv}$value") }
        writer.close()
    }

    fun getLanguages(): Array<String> = languages.keys.toTypedArray()
    fun getLangName(): String = Settings.getProperty("langName")!!
    fun setLangName(value: String) {
        if (languages.containsKey(value))
            Settings.setProperty("langName", value)
    }
}