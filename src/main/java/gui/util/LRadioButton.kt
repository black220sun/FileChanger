package gui.util

import settings.Settings
import javax.swing.JRadioButton

class LRadioButton(name: String, state: Boolean = false) : JRadioButton(Settings.getLang(name), state)
