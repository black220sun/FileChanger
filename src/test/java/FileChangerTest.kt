import filechanger.FileChanger
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileChangerTest {
    @Test
    fun translateNoExtensionTest() {
        val file = File("testoVaJa prograMMa")
        file.createNewFile()
        val changer = FileChanger()
        changer.loadTranslation()

        val new = changer.translate(file)

        assertTrue(new.exists())
        assertEquals("тестоВаЯ програММа", new.name)
        assertFalse(file.exists())

        new.delete()
    }
    @Test
    fun translateExtensionTest() {
        val file = File("Jupiter - Devushka po gorodu shagaet bosikom.mp3")
        file.createNewFile()
        val changer = FileChanger()
        changer.loadTranslation()

        val new = changer.translate(file)

        assertTrue(new.exists())
        assertEquals("Юпитер - Девушка по городу шагает босиком.mp3", new.name)
        assertFalse(file.exists())

        new.delete()
    }
    @Test
    fun renameTest() {
        val file = File("AlisA_-_Rodina_(zf.fm).mp3")
        file.createNewFile()
        val changer = FileChanger()
        changer.addReplacement("_", " ")
        changer.addReplacement(" (zf.fm)", "")

        val new = changer.rename(file)

        assertTrue(new.exists())
        assertEquals("AlisA - Rodina.mp3", new.name)
        assertFalse(file.exists())

        new.delete()
    }
    @Test
    fun moveTest() {
        val file = File("AlisA_-_Rodina.mp3")
        file.createNewFile()
        val changer = FileChanger()
        val dir = File("AlisA")
        dir.mkdir()

        val new = changer.move(file, "AlisA")

        assertTrue(new.exists())
        assertFalse(file.exists())
        assertEquals("AlisA", new.parentFile.name)
        assertEquals(dir.absolutePath, new.parentFile.absolutePath)

        new.delete()
        dir.delete()
    }
    @Test
    fun moveAbsolutePathTest() {
        val file = File("AlisA_-_Rodina.mp3")
        file.createNewFile()
        val changer = FileChanger()
        val dir = File("AlisA")
        dir.mkdir()

        val new = changer.move(file, "${System.getProperty("user.home")}/IdeaProjects/FileChanger/AlisA")

        assertTrue(new.exists())
        assertFalse(file.exists())
        assertEquals("AlisA", new.parentFile.name)
        assertEquals(dir.absolutePath, new.parentFile.absolutePath)

        new.delete()
        dir.delete()
    }
    @Test
    fun moveByNameToTest() {
        val file = File("AlisA_-_Rodina.mp3")
        file.createNewFile()
        val changer = FileChanger()

        val new = changer.moveByName(file, to = "_-_")

        assertTrue(new.exists())
        assertFalse(file.exists())
        assertTrue(File("AlisA").isDirectory)
        assertEquals("AlisA", new.parentFile.name)

        new.delete()
        File("AlisA").delete()
    }
    @Test
    fun moveByNameFromToTest() {
        val file = File("2._AlisA_-_Rodina.mp3")
        file.createNewFile()
        val changer = FileChanger()

        val new = changer.moveByName(file, from = "._", to = "_-_")

        assertTrue(new.exists())
        assertTrue(File("AlisA").isDirectory)
        assertEquals("AlisA", new.parentFile.name)

        new.delete()
        File("AlisA").delete()
    }
    @Test
    fun moveByNameTest() {
        val file = File("2._AlisA_-_Rodina.mp3")
        file.createNewFile()
        val changer = FileChanger()

        val new = changer.moveByName(file, from = "abc", to = "abc")

        assertEquals(file, new)

        new.delete()
    }
    @Test
    fun renameRegexTest() {
        val file = File("AlisA_-_Rodina_(zf.fm).mp3")
        file.createNewFile()
        val changer = FileChanger()
        changer.addRegex("_", " ")
        changer.addRegex(" \\(.*\\)", "")

        val new = changer.rename(file, true)

        assertTrue(new.exists())
        assertEquals("AlisA - Rodina.mp3", new.name)
        assertFalse(file.exists())

        new.delete()
    }
    @Test
    fun directoryRenameTest() {
        val file = File("AlisA/AlisA_-_Rodina_(zf.fm).mp3")
        val dir = File("AlisA")
        dir.mkdir()
        file.createNewFile()
        val changer = FileChanger()
        changer.addRegex("_", " ")
        changer.addRegex(" \\(.*\\)", "")

        val new = changer.rename(file, true)

        assertTrue(new.exists())
        assertEquals("AlisA - Rodina.mp3", new.name)
        assertEquals("AlisA", new.parentFile.name)
        assertFalse(file.exists())

        new.delete()
        dir.delete()
    }
}