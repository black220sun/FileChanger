package gui2.toolbar

import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
import gui.util.LPattern
import settings.Settings
import java.awt.GridLayout
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextField

class ConvertTab: JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        val label = LLabel("Pattern:")
        label.toolTipText = Settings.getLang("%n - title; %a - artist; %m - album; %y - year; %t - track; %g - genre")
        add(label)

        val patterns = LPattern()

        val pattern = JTextField(patterns.selectedItem as String? ?: "")
        add(pattern)

        val panel = JPanel()
        panel.layout = GridLayout(3, 0)
        add(panel)

        val title = LButton("Title")
        title.addActionListener { pattern.insert("%n") }
        panel.add(title)

        val artist = LButton("Artist")
        artist.addActionListener { pattern.insert("%a") }
        panel.add(artist)

        patterns.addActionListener { pattern.text = patterns.selectedItem as String }
        panel.add(patterns)

        val ignore = LCheckBox("Ignore empty tags", true)
        panel.add(ignore)

        val album = LButton("Album")
        album.addActionListener { pattern.insert("%m") }
        panel.add(album)

        val year = LButton("Year")
        year.addActionListener { pattern.insert("%y") }
        panel.add(year)

        val save = LButton("Save")
        save.addActionListener { patterns.addText(pattern.text) }
        panel.add(save)

        val tagName = LButton("Tags to name")
        tagName.addActionListener {

        }
        panel.add(tagName)

        val track = LButton("Track")
        track.addActionListener { pattern.insert("%t") }
        panel.add(track)

        val genre = LButton("Genre")
        genre.addActionListener { pattern.insert("%g") }
        panel.add(genre)

        val remove = LButton("Remove")
        remove.addActionListener { patterns.removeText() }
        panel.add(remove)

        val nameTag = LButton("Name to tags")
        nameTag.addActionListener {

        }
        panel.add(nameTag)
    }

    private fun JTextField.insert(s: String) {
        val index = caretPosition
        text = text.substring(0, index) + s + text.substring(index)
        caretPosition = index + s.length
        requestFocus()
    }
}
