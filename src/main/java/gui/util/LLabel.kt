package gui.util

import settings.Settings
import javax.swing.JLabel

class LLabel(name: String) : JLabel(Settings.getLang(name))