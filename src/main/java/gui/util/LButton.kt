package gui.util

import settings.Settings
import javax.swing.JButton

class LButton(name: String, optional: String = "") : JButton(Settings.getLang(name) + optional)