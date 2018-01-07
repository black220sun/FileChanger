package gui.util

import settings.Settings
import javax.swing.JButton

class LButton(name: String) : JButton(Settings.getLang(name))