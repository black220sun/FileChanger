package gui2.toolbar

import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import gui.util.LRadioButton
import mp3tag.TagReader
import java.awt.GridLayout
import javax.swing.*

class TagsTab : JScrollPane() {
    private val title = JTextField()
    private val artist = JTextField()
    private val album = JTextField()
    private val year = JTextField()
    private val track = JTextField()
    private val genre = JTextField()
    private val tagFields = ArrayList<JTextField>()
    private val enables = ArrayList<JCheckBox>()
    private val caps = ArrayList<ButtonGroup>()
    init {
        val panel = JPanel()
        viewport.view = panel
        panel.layout = GridLayout(0, 6)

        val reg = LCheckBox("Change register", true)
        panel.add(reg)


        val process = LButton("Process")
        process.addActionListener { /*TODO(implement)*/ }
        panel.add(process)
        val select = LCheckBox("All")
        select.addActionListener {
            enables.forEachIndexed {
                index, jCheckBox -> jCheckBox.isSelected = select.isSelected
                tagFields[index].isEnabled = select.isSelected
            }
        }
        panel.add(select)

        val all = ButtonGroup()
        val noneAll = LRadioButton("Don`t change")
        noneAll.addActionListener {
            if ((it.source as JRadioButton).isSelected)
                caps.forEach { it.elements.toList()[0].isSelected = true }
        }
        val firstAll = LRadioButton("First letter", true)
        firstAll.addActionListener {
            if ((it.source as JRadioButton).isSelected)
                caps.forEach { it.elements.toList()[1].isSelected = true }
        }
        val eachAll = LRadioButton("Each Letter")
        eachAll.addActionListener {
            if ((it.source as JRadioButton).isSelected)
                caps.forEach { it.elements.toList()[2].isSelected = true }
        }
        all.add(noneAll)
        all.add(firstAll)
        all.add(eachAll)
        panel.add(noneAll)
        panel.add(firstAll)
        panel.add(eachAll)

        reg.addActionListener {
            noneAll.isEnabled = reg.isSelected
            firstAll.isEnabled = reg.isSelected
            eachAll.isEnabled = reg.isSelected
            caps.forEach {
                it.elements.toList().forEach { it.isEnabled = reg.isSelected }
            }
        }

        panel.add(LLabel("Artist"))
        panel.add(artist)
        val bArtist = JCheckBox()
        panel.add(bArtist)
        tagFields.add(artist)
        enables.add(bArtist)
        panel.group()

        panel.add(LLabel("Title"))
        panel.add(title)
        val bTitle = JCheckBox()
        panel.add(bTitle)
        tagFields.add(title)
        enables.add(bTitle)
        panel.group()

        panel.add(LLabel("Album"))
        panel.add(album)
        val bAlbum= JCheckBox()
        panel.add(bAlbum)
        tagFields.add(album)
        enables.add(bAlbum)
        panel.group()

        panel.add(LLabel("Year"))
        panel.add(year)
        val bYear = JCheckBox()
        panel.add(bYear)
        tagFields.add(year)
        enables.add(bYear)
        panel.group()

        panel.add(LLabel("Track"))
        panel.add(track)
        val bTrack = JCheckBox()
        panel.add(bTrack)
        tagFields.add(track)
        enables.add(bTrack)
        panel.group()

        panel.add(LLabel("Genre"))
        panel.add(genre)
        val bGenre = JCheckBox()
        panel.add(bGenre)
        tagFields.add(genre)
        enables.add(bGenre)
        panel.group()

        tagFields.forEach { it.isEnabled = false }
        (0 until tagFields.size).forEach { i ->
            enables[i].addActionListener { tagFields[i].isEnabled = (it.source as JCheckBox).isSelected }
        }
    }

    fun fill(tags: TagReader.TagsData) {
        title.text = tags.title()
        artist.text = tags.artist()
        album.text = tags.album()
        year.text = tags.year().toString()
        genre.text = tags.genre()
        track.text = tags.track().toString()
    }

    private fun JPanel.group() {
        val group = ButtonGroup()
        val btn1 = JRadioButton()
        btn1.actionCommand = "0"
        val btn2 = JRadioButton()
        btn2.isSelected = true
        btn2.actionCommand = "1"
        val btn3 = JRadioButton()
        btn3.actionCommand = "2"
        group.add(btn1)
        group.add(btn2)
        group.add(btn3)
        add(btn1)
        add(btn2)
        add(btn3)
        caps.add(group)
    }
}

