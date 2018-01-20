package settings

class SizeResolver {
    fun getSize(key: String): Int? {
        if (checkKey(key))
            return Settings.getProperty(key)?.toIntOrNull()
        return null
    }
    fun setSize(key: String, value: Int) {
        if (checkKey(key))
            Settings.setProperty(key, value.toString())
    }

    private fun checkKey(key: String): Boolean = key.startsWith("size")
}