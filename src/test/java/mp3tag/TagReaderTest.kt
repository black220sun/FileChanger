package mp3tag

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class TagReaderTest {
    private val testName = "Hollywood Undead - Apologize.mp3"
    @Test
    fun readTags() {
        val reader = TagReader
        val file = File(testName)

        val tags = reader.readTags(file)

        assertEquals("hollywood undead", tags["TPE1"])
    }
    @Test
    fun writeTags() {
        val reader = TagReader
        val file = File(testName)

        val tags = reader.readTags(file)
        val name = tags["TIT2"]!!
        tags["TIT2"] = "Тест"

        reader.writeTags(file, tags)
        val new = reader.readTags(file)

        assertEquals(tags["TIT2"], new["TIT2"])

        tags["TIT2"] = name
        reader.writeTags(file, tags)

        assertEquals(name, reader.readTags(file)["TIT2"])

    }
}