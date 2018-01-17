package mp3tag

import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.charset.Charset

object TagReader {
    private val charsets = HashMap<Byte, Charset>()
    private val id3 = "ID3"
    val title = "TIT2"
    val artist = "TPE1"
    val track = "TRCK"
    val album = "TALB"
    val genre = "TCON"
    val year = "TYER"
    val date = "TDRC"
    enum class Encoding {
        ISO, UTF16LE, UTF16BE, UTF8
    }

    class TagsData : HashMap<String, String>() {
        fun title() = getOrDefault(title, "")
        fun artist() = getOrDefault(artist, "")
        fun album() = getOrDefault(album, "")
        fun year() = if (v4())
                getOrDefault(date, "")
            else
                getOrDefault(year, "")
        fun genre() = getOrDefault(genre, "")
        fun track() = getOrDefault(track, "")
        fun toList(): List<Any> = arrayListOf(title(), artist(), album(), year(), track(), genre())
        fun index(i: Int, value: Any) {
            val real = value.toString()
            when (i) {
                0 -> put(title, real)
                1 -> put(artist, real)
                2 -> put(album, real)
                3 -> put(if (v4()) date else year, real)
                4 -> put(track, real)
                5 -> put(genre, real)
            }
        }
        private fun v4(): Boolean = get("version") == "4"
    }

    init {
        charsets.put(0, Charset.forName("ISO-8859-1"))
        charsets.put(1, Charset.forName("UTF-16LE"))
        charsets.put(2, Charset.forName("UTF-16BE"))
        charsets.put(3, Charset.forName("UTF-8"))
    }

    fun readTags(file: File): TagsData {
        val tags = TagsData()
        if (!file.exists() || file.isDirectory)
            return tags
        val header = ByteArray(10)
        FileInputStream(file).use {
            if (it.read(header) == -1)
                return@use
            if (String(header.copyOfRange(0, 3)) != id3)
                return@use
            tags.put("type", id3)
            tags.put("version", header[3].toString())
            val flags = header[5]
            tags.put("flags", flags.toString())
            var size = header[9] + (header[8] + (header[7] + header[6] * 128) * 128) * 128
            tags.put("size", size.toString())
            do {
                size = it.readFrame(size, tags)
            } while (size > 0)
        }
        return tags
    }

    fun writeTags(file: File, tags: TagsData) {
        if (!file.exists())
            return
        val header = ByteArray(10)
        RandomAccessFile(file, "rw").use {
            if (it.read(header) == -1)
                return
            if (String(header.copyOfRange(0, 3)) != tags["type"])
                return
            if (header[3].toString() != tags["version"])
                return
            var total = header[9] + (header[8] + (header[7] + header[6] * 128) * 128) * 128
            it.seek(3)
            it.writeByte(4)
            it.seek(10)
            tags.filter { it.key.matches(Regex("[A-Z0-9]{4}")) }.forEach {
                key, value -> run {
                val bytes = (value + 0.toChar()).toByteArray(charsets[3]!!)
                var size = bytes.size + 1
                total -= 10 + size
                if (total < 0)
                    return@forEach
                val arr = ByteArray(4)
                for (i in 3 downTo 0) {
                    arr[i] = (size % 128).toByte()
                    size /= 128
                }
                it.write(key.toByteArray())
                it.write(arr)
                it.write(ByteArray(2))
                it.write(ByteArray(1, { 3 }))
                it.write(bytes)
                }
            }
            if (total > 0)
                it.write(ByteArray(total))
        }

    }

    private fun FileInputStream.readFrame(total: Int, tags: TagsData): Int {
        val header = ByteArray(10)
        if (read(header) == -1)
            return -1
        if (header.all { it == (0).toByte() }) {
            return -1
        }
        val name = String(header.copyOfRange(0, 4))
        if (!name.matches(Regex("[A-Z0-9]{4}")))
            return -1
        val size = header[7] + (header[6] + (header[5] + header[4] * 128) * 128) * 128
        if (size < 0)
            return -1
        val value = ByteArray(size)
        read(value)
        val text = getText(value)
        tags.put(name, text)
        return total - 10 - size
    }

    private fun getText(value: ByteArray): String {
        val index = value.lastIndex + 1
        if (index < 2)
            return ""
        val start = if (value[0].toInt() == 1) 3 else 1
        val newValue = value.copyOfRange(start, index)
        val charset = charsets[value[0]] ?: return String(value, Charset.forName("UTF-8"))
        var text = String(newValue, charset)
        if (text.last() == 0.toChar())
            text = text.dropLast(1)
        val regex = Regex("[-A-Za-z0-9а-яА-ЯїєёЁъЪіЇЄІ,.+/\\\\_&?*%@!$#^=:'\"`~)( \t]+")
        if (text.matches(regex))
            return text
        val cyrillicText = String(newValue, Charset.forName("windows-1251"))
        return if (cyrillicText.matches(regex))
            cyrillicText
        else
            ""
    }
}