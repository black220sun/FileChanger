import filechanger.FileChanger
import org.junit.Test
import java.io.File

class FileChangerTest {
    @Test
    fun translateNoExtensionTest() {
        val file = File("testoVaJa prograMMa")
        file.createNewFile()
        val changer = FileChanger()
        changer.load()

        val new = changer.translate(file)

        assert(new.exists())
        assert(new.name == "тестоВаЯ програММа")
        assert(!file.exists())

        new.delete()
    }
    @Test
    fun translateExtensionTest() {
        val file = File("Jupiter - Devushka po gorodu shagaet bosikom.mp3")
        file.createNewFile()
        val changer = FileChanger()
        changer.load()

        val new = changer.translate(file)

        assert(new.exists())
        assert(new.name == "Юпитер - Девушка по городу шагает босиком.mp3")
        assert(!file.exists())

        new.delete()
    }
}