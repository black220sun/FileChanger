package mp3tag

import settings.Settings
import java.io.File
import java.util.regex.Pattern
import mp3tag.TagReader as tr

object TagCreator {
    private val separator = Settings.separator

    fun tagToName(file: File, pattern: String, force: Boolean,
                  capitalize: List<Boolean>, ignore: Boolean,
                  delimiter: String): File {
        val tags = tr.readTags(file)

        val artist = tags.getOrDefault(tr.artist, "")
        val title = tags.getOrDefault(tr.title, "")
        val album = tags.getOrDefault(tr.album, "")
        val genre = tags.getOrDefault(tr.genre, "")
        val year = tags.getOrDefault(tr.year, "")
        var track = tags.getOrDefault(tr.track, "")
        track = when  {
            track == "" -> ""
            track.matches(Regex("\\d*")) -> track
            else -> track.subSequence(0, track.indexOfFirst { it !in '0'..'9' }) as String
        }
        val values = arrayListOf(title, artist, album, genre, year, track)
        val capFirst = capitalize.subList(0, 4)
        val capAll = capitalize.subList(4, 8)
        if (ignore) {
            val patterns = arrayListOf("%n", "%t", "%m", "%g", "%y", "%t")
            (0..5).forEach {
                if (pattern.contains(patterns[it]) && values[it].isEmpty())
                    return file
            }
        }
        (0..3).forEach {
            if (capFirst[it])
                values[it] = values[it].capitalize()
            else if (capAll[it])
                values[it] = values[it].split(delimiter).joinToString(delimiter) { it.capitalize() }
        }
        val new = pattern.replace("%a", artist)
                .replace("%y", year)
                .replace("%m", album)
                .replace("%g", genre)
                .replace("%n", title)
                .replace("%t", track)
        val path = file.absoluteFile.parentFile.absolutePath
        val extension = if (file.extension == "") "" else "." + file.extension
        val newFile = File(path + separator + new + extension)
        if (force) {
            newFile.absoluteFile.parentFile.mkdirs()
            file.renameTo(newFile)
        }
        return newFile
    }

    fun nameToTag(file: File, pattern: String, force: Boolean, capitalize: List<Boolean>, delimiter: String): Map<String, String> {
        val tags = tr.readTags(file)
        val matcher = Pattern.compile("%[amygnt]").matcher(pattern)
        val regex = pattern.replace(Regex("%[amygnt]"), "(.*)").toRegex()
        var name = if (pattern.contains(separator)) file.absoluteFile.parentFile.name + separator else ""
        name += file.nameWithoutExtension
        if (!name.matches(regex))
            return tags

        var artist = ""
        var title = ""
        var album = ""
        var genre = ""
        var year = ""
        var track = ""
        var i = 0
        var j = 0
        while (true) {
            var value = ""
            while (pattern[i] == name[j]) {
                ++i
                ++j
            }
            if (pattern[i] == '%' && pattern[i + 1] in arrayOf('a', 'm', 'y', 'g', 'n', 't')) {
                i += 2
                if (i >= pattern.length)
                    value = name.substring(j)
                else
                    while (name[j] != pattern[i])
                        value += name[j++]
            }
            if (matcher.find())
                when (matcher.group()) {
                    "%a" -> artist = value
                    "%g" -> genre = value
                    "%m" -> album = value
                    "%n" -> title = value
                    "%t" -> track = value
                    else -> year = value
                }
            if (i >= pattern.length)
                break
        }
        if (capitalize[7])
            genre = genre.split(delimiter).joinToString(delimiter) { it.capitalize() }
        else if (capitalize[3])
            genre = genre.capitalize()
        if (capitalize[6])
            album = album.split(delimiter).joinToString(delimiter) { it.capitalize() }
        else if (capitalize[2])
            album = album.capitalize()
        if (capitalize[5])
            artist = artist.split(delimiter).joinToString(delimiter) { it.capitalize() }
        else if (capitalize[1])
            artist = artist.capitalize()
        if (capitalize[4])
            title = title.split(delimiter).joinToString(delimiter) { it.capitalize() }
        else if (capitalize[0])
            title = title.capitalize()
        if (artist.isNotEmpty()) tags.put(tr.artist, artist)
        if (album.isNotEmpty()) tags.put(tr.album, album)
        if (genre.isNotEmpty()) tags.put(tr.genre, genre)
        if (title.isNotEmpty()) tags.put(tr.title, title)
        if (track.isNotEmpty()) tags.put(tr.track, track)
        if (year.isNotEmpty()) tags.put(if (tags["version"] == "4") tr.date else tr.year, year)
        if (force)
            tr.writeTags(file, tags)
        return tags
    }
}