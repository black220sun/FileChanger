package gui.util

import settings.Settings
import javax.swing.JCheckBoxMenuItem

class LCheckBoxMenuItem(name: String, state: Boolean = false) : JCheckBoxMenuItem(Settings.getLang(name), state)