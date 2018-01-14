package gui.tabs

import gui.MainController
import gui.TableModel
import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import mp3tag.TagCreator
import settings.Settings
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextField
import java.io.File
import java.util.stream.Collector
import java.util.stream.Collectors

class TagsProcessingTab : JPanel() {
    init {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val label = LLabel("Pattern:")
        label.toolTipText = Settings.getLang("%n - title; %a - artist; %m - album; %y - year; %t - track; %g - genre")
        panel.add(label)

        val pattern = JTextField()
        panel.add(pattern)

        panel.add(LLabel("Capitalize"))

        val capAllFirst = LCheckBox("All first")
        panel.add(capAllFirst)
        val capAll = LCheckBox("All")
        panel.add(capAll)
        val capTitleFirst = LCheckBox("Title first letter", true)
        val capArtistFirst = LCheckBox("Artist first letter")
        val capAlbumFirst = LCheckBox("Album first letter", true)
        val capGenreFirst = LCheckBox("Genre first letter", true)
        val capsFirst = arrayOf(capTitleFirst, capArtistFirst, capAlbumFirst, capGenreFirst)
        capsFirst.forEach { panel.add(it) }
        val capTitle = LCheckBox("Title")
        val capArtist = LCheckBox("Artist", true)
        val capAlbum = LCheckBox("Album")
        val capGenre = LCheckBox("Genre")
        val caps = arrayOf(capTitle, capArtist, capAlbum, capGenre)
        caps.forEach { panel.add(it) }
        capAll.addActionListener {
            if (capAll.isSelected)
                caps.forEach {
                    it.isSelected = true
                    it.isEnabled = false
                }
            else
                caps.forEach { it.isEnabled = true }
        }
        capAllFirst.addActionListener {
            if (capAllFirst.isSelected)
                capsFirst.forEach {
                    it.isSelected = true
                    it.isEnabled = false
                }
            else
                capsFirst.forEach { it.isEnabled = true }
        }

        val delim = LCheckBox("Words delimiter")
        panel.add(delim)

        val delimiter = JTextField("")
        delimiter.isEnabled = false
        panel.add(delimiter)

        delim.addActionListener { delimiter.isEnabled = delim.isSelected }

        val tagName = LButton("Tags to name")
        tagName.addActionListener {
            val capitalize = (capsFirst + caps).map { it.isSelected }
            val force = Settings.getForce("forceTag")
            val files = TableModel.changer.getFiles()
            val result = files.parallelStream().map {
                TagCreator.tagToName(it, pattern.text,
                        force, capitalize,
                        if (delim.isSelected) delimiter.text else " ")
            }.toArray().toList() as List<File>
            MainController.results(arrayListOf(files,result), force)
        }
        panel.add(tagName)

        val nameTag = LButton("Name to tags")
        nameTag.addActionListener {
            val capitalize = (capsFirst + caps).map { it.isSelected }
            val force = Settings.getForce("forceTag")
            val files = TableModel.changer.getFiles()
            val new = files.parallelStream().map {
                TagCreator.nameToTag(it, pattern.text,
                        force, capitalize,
                        if (delim.isSelected) delimiter.text else " ")
            }.toArray().toList() as List<Map<String, String>>
            MainController.tags(arrayListOf(files, new))
        }
        panel.add(nameTag)

        add(panel)
    }
}