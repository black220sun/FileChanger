package mp3tag

import settings.Settings
import java.io.File
import mp3tag.TagReader as tr

object TagCreator {
    private val separator = Settings.separator

    fun tagToName(file: File, pattern: String, force: Boolean = true,
                  capitalize: Collection<Boolean> = List(8, {false}),
                  delimiter: String = " "): File {
        val tags = tr.readTags(file)

        var artist = tags.getOrDefault(tr.artist, "")
        var title = tags.getOrDefault(tr.title, "")
        var album = tags.getOrDefault(tr.album, "")
        var genre = tags.getOrDefault(tr.genre, "")
        val year = tags.getOrDefault(tr.year, "")
        var track = tags.getOrDefault(tr.track, "")
        track = when  {
            track == "" -> ""
            track.matches(Regex("\\d*")) -> track
            else -> track.subSequence(0, track.indexOfFirst { it !in '0'..'9' }) as String
        }
        when {
            capitalize.elementAt(7) -> genre = genre.split(delimiter).joinToString(delimiter) { it.capitalize() }
            capitalize.elementAt(3) -> genre = genre.capitalize()
            capitalize.elementAt(6) -> album = album.split(delimiter).joinToString(delimiter) { it.capitalize() }
            capitalize.elementAt(2) -> album = album.capitalize()
            capitalize.elementAt(5) -> title = title.split(delimiter).joinToString(delimiter) { it.capitalize() }
            capitalize.elementAt(1) -> title = title.capitalize()
            capitalize.elementAt(4) -> artist = artist.split(delimiter).joinToString(delimiter) { it.capitalize() }
            capitalize.elementAt(0) -> artist = artist.capitalize()
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
        if (force)
            file.renameTo(newFile)
        return newFile
    }
}