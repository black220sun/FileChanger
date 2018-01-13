package mp3tag

import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.charset.Charset

object TagReader {
    private val charsets = arrayOf("UTF-16", "UTF-8", "UTF-16LE", "UTF-16BE", "ISO-8859-1",
            "US-ASCII", "windows-1251", "windows-1252")
            .map { charset(it)  }
    private val defaultCharset = charset("UTF8")
    private val id3 = "ID3"
    val title = "TIT2"
    val artist = "TPE1"
    val track = "TRCK"
    val album = "TALB"
    val genre = "TCON"

    fun readTags(file: File): HashMap<String, String> {
        val tags = HashMap<String, String>()
        if (!file.exists())
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

    fun writeTags(file: File, tags: HashMap<String, String>, charset: Charset = defaultCharset) {
        if (!file.exists())
            return
        val header = ByteArray(10)
        var total = 0
        FileInputStream(file).use {
            if (it.read(header) == -1)
                return
            if (String(header.copyOfRange(0, 3)) != tags["type"])
                return
            if (header[3].toString() != tags["version"])
                return
            total = header[9] + (header[8] + (header[7] + header[6] * 128) * 128) * 128
        }
        RandomAccessFile(file, "rw").use {
            it.seek(10)
            tags.filter { it.key.matches(Regex("[A-Z0-9]{4}")) }.forEach {
                key, value -> run {
                var size = value.toByteArray(charset).size + 1
                total -= 10 + size
                if (total <= 0)
                    return@forEach
                val arr = ByteArray(4)
                for (i in 3 downTo 0) {
                    arr[i] = (size % 128).toByte()
                    size /= 128
                }
                it.write(key.toByteArray())
                it.write(arr)
                it.write(ByteArray(2))
                it.write(ByteArray(1, {(3).toByte()}))
                it.write(value.toByteArray(charset))
                }
            }
            if (total > 0)
                it.write(ByteArray(total))
        }

    }

    private fun FileInputStream.readFrame(total: Int, tags: HashMap<String, String>): Int {
        val header = ByteArray(10)
        if (read(header) == -1)
            return -1
        val name = String(header.copyOfRange(0, 4))
        if (header.all { it == (0).toByte() }) {
            return -1
        }
        val size = header[7] + (header[6] + (header[5] + header[4] * 128) * 128) * 128
        if (size < 0)
            return -1
        val value = ByteArray(size)
        read(value)
        tags.put(name, getText(value))
        return total - 10 - size
    }

    private fun getText(value: ByteArray): String {
        val index = value.lastIndex
        if (index < 1)
            return ""
        charsets.forEach {
            val text = String(value.copyOfRange(1, index), it)
            if (text.matches(Regex("[-A-Za-z0-9а-яА-ЯїєёЁъЪіЇЄІ,.+/\\\\_&?*%@!$#^=:'\"`~)( \t]+")))
                return text
            if (text.matches(Regex("[-A-Za-z0-9а-яА-ЯїєёЁъЪіЇЄІ,.+/\\\\_&?*%@!$#^=:'\"`~)( \t]+.")))
                return text.dropLast(1) + String(ByteArray(1, { value.last() }), defaultCharset)
        }
        return ""
    }
}