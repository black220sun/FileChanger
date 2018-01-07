package gui.util

import settings.Settings
import javax.swing.JMenuItem

class LMenuItem(name: String) : JMenuItem(Settings.getLang(name))