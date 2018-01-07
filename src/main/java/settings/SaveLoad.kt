package settings

class SaveLoad {
    private val path = Settings.getDirectory() + "files"
    fun getSaveLoad(): Boolean = Settings.getProperty("saveLoad")?.toBoolean() ?: false
    fun setSaveLoad(state: Boolean) = Settings.setProperty("saveLoad", state.toString())
    fun getPath(): String = path
}