package gui.util

import settings.Settings
import javax.swing.JCheckBox

class LCheckBox(name: String, state: Boolean = false) : JCheckBox(Settings.getLang(name), state)