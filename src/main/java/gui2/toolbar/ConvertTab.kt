package gui2.toolbar

import gui.util.LButton
import gui.util.LCheckBox
import gui.util.LLabel
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

        val pattern = JTextField()
        add(pattern)

        val panel = JPanel()
        panel.layout = GridLayout(3, 0)
        add(panel)

        val title = LButton("Title")
        title.addActionListener { pattern.text += "%n" }
        panel.add(title)

        val artist = LButton("Artist")
        artist.addActionListener { pattern.text += "%a" }
        panel.add(artist)

        panel.add(LLabel(""))

        val ignore = LCheckBox("Ignore empty tags", true)
        panel.add(ignore)

        val album = LButton("Album")
        album.addActionListener { pattern.text += "%m" }
        panel.add(album)

        val year = LButton("Year")
        year.addActionListener { pattern.text += "%y" }
        panel.add(year)

        panel.add(LLabel(""))

        val tagName = LButton("Tags to name")
        tagName.addActionListener {

        }
        panel.add(tagName)

        val track = LButton("Track")
        track.addActionListener { pattern.text += "%t" }
        panel.add(track)

        val genre = LButton("Genre")
        genre.addActionListener { pattern.text += "%g" }
        panel.add(genre)

        panel.add(LLabel(""))

        val nameTag = LButton("Name to tags")
        nameTag.addActionListener {

        }
        panel.add(nameTag)
    }
}