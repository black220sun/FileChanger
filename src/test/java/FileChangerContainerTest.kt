import filechanger.FileChangerContainer
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileChangerContainerTest {
    @Test
    fun rename() {
        val changer = FileChangerContainer()
        val file1 = File("test_(abc) 123")
        val file2 = File("programm_(test)")
        val files = arrayListOf(file1, file2)
        files.forEach { it.createNewFile() }
        changer.addFiles(files)
        changer.add("_\\(.*\\)", "", FileChangerContainer.Type.REGEX)
        val expected = listOf(File("test 123"), File("programm")).map { it.absoluteFile }

        val new = changer.rename(regex = true)

        assertEquals(expected, new)
        new.forEach { assertTrue { it.exists() } }
        files.forEach { assertFalse { it.exists() } }

        new.forEach { it.delete() }
    }

    @Test
    fun translate() {
        val changer = FileChangerContainer()
        val file1 = File("test.1").absoluteFile
        val file2 = File("programma.2")
        val file3 = File("test.2")
        val file4 = File("pesnja.2")
        val files = arrayListOf(file1, file2)
        files.forEach { it.createNewFile() }
        file3.createNewFile()
        file4.createNewFile()
        changer.init()
        changer.addFiles(files)
        changer.addFile(file3)
        changer.addFile("pesnja.2")
        val expected = listOf(file1, File("тест.2"), File("программа.2"), File("песня.2")).map { it.absoluteFile }

        val new = changer.translate( { it.extension == "2" } )

        new.forEach { assertTrue { it.exists() } }
        assertFalse { File("тест.1").exists() }
        listOf(file2, file3, file4).forEach { assertFalse { it.exists() } }

        expected.forEach { it.delete() }
    }

    @Test
    fun move() {
        val changer = FileChangerContainer()
        val file1 = File("test 123")
        val file2 = File("programm")
        val files = arrayListOf(file1, file2)
        val dir = File("test_dir")
        dir.mkdir()
        files.forEach { it.createNewFile() }
        changer.addFiles(files)
        val expected = listOf(File("test_dir/test 123"), File("test_dir/programm")).map { it.absoluteFile }

        val new = changer.move(dir)

        assertEquals(expected, new)
        new.forEach { assertTrue { it.exists() } }
        files.forEach { assertFalse { it.exists() } }

        new.forEach { it.delete() }
        dir.delete()
    }

    @Test
    fun moveByName() {
        val changer = FileChangerContainer()
        val file1 = File("photo_123.png")
        val file2 = File("photo_124.png")
        val file3 = File("music_111.mp3")
        val files = arrayListOf(file1, file2, file3)
        val dir1 = File("photo")
        val dir2 = File("music")
        val dirs = listOf(dir1, dir2)
        files.forEach { it.createNewFile() }
        changer.addFiles(files)
        val expected = listOf(File("photo/photo_123.png"),
                File("photo/photo_124.png"),
                File("music/music_111.mp3"))

        val new = changer.moveByName(to = "_")

        assertEquals(expected, new)
        dirs.forEach { assertTrue { it.exists() } }
        dirs.forEach { assertTrue { it.isDirectory } }
        new.forEach { assertTrue { it.exists() } }
        files.forEach { assertFalse { it.exists() } }

        new.forEach { it.delete() }
        dirs.forEach { it.delete() }
    }

    @Test
    fun loadFiles() {
        val changer = FileChangerContainer()
        val file1 = File("photo_123.png")
        val file2 = File("photo_124.png")
        val file3 = File("music_111.mp3")
        val files = arrayListOf(file1, file2, file3)
        files.forEach { it.createNewFile() }
        changer.addFiles(files)
        val expected = files.map { it.absoluteFile }

        changer.saveFiles("files.sv")
        changer.removeFiles { true }
        changer.loadFiles("files.sv")

        assertEquals(expected, changer.getFiles())

        files.forEach { it.delete() }
        File("files.sv").delete()
    }

    @Test
    fun removeFile() {
        val changer = FileChangerContainer()
        val file1 = File("photo_123.png")
        val file2 = File("photo_124.png")
        val file3 = File("music_111.mp3")
        val files = arrayListOf(file1, file2, file3)
        files.forEach { it.createNewFile() }
        changer.addFiles(files)
        val expected = listOf(file1, file3)

        changer.removeFile(file2)

        assertEquals(expected, changer.getFiles())

        files.forEach { it.delete() }
    }

    @Test
    fun removeFiles() {
        val changer = FileChangerContainer()
        val file1 = File("photo_123.png")
        val file2 = File("photo_124.png")
        val file3 = File("music_111.mp3")
        val files = arrayListOf(file1, file2, file3)
        files.forEach { it.createNewFile() }
        changer.addFiles(files)
        val expected = listOf(file3)

        changer.removeFiles { it.name.matches("photo.*".toRegex()) }

        assertEquals(expected, changer.getFiles())

        files.forEach { it.delete() }
    }

}