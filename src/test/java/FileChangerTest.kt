import filechanger.FileChanger
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class FileChangerTest {
    @Test
    fun translateNoExtensionTest() {
        val file = File("testoVaJa prograMMa")
        file.createNewFile()
        val changer = FileChanger()
        changer.loadTranslation()

        val new = changer.translate(file)

        assertEquals(true, new.exists())
        assertEquals("тестоВаЯ програММа", new.name)
        assertEquals(false, file.exists())

        new.delete()
    }
    @Test
    fun translateExtensionTest() {
        val file = File("Jupiter - Devushka po gorodu shagaet bosikom.mp3")
        file.createNewFile()
        val changer = FileChanger()
        changer.loadTranslation()

        val new = changer.translate(file)

        assertEquals(true, new.exists())
        assertEquals("Юпитер - Девушка по городу шагает босиком.mp3", new.name)
        assertEquals(false, file.exists())

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

        assertEquals(true, new.exists())
        assertEquals("AlisA - Rodina.mp3", new.name)
        assertEquals(false, file.exists())

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

        assertEquals(true, new.exists())
        assertEquals(false, file.exists())
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

        assertEquals(true, new.exists())
        assertEquals(false, file.exists())
        assertEquals("AlisA", new.parentFile.name)
        assertEquals(dir.absolutePath, new.parentFile.absolutePath)

        new.delete()
        dir.delete()
    }
    @Test
    fun moveByNameTest() {
        val file = File("AlisA_-_Rodina.mp3")
        file.createNewFile()
        val changer = FileChanger()

        val new = changer.moveByName(file, delimiter = "_-_")

        assertEquals(true, new.exists())
        assertEquals(false, file.exists())
        assertEquals(true, File("AlisA").isDirectory)
        assertEquals("AlisA", new.parentFile.name)

        new.delete()
        File("AlisA").delete()
    }
}