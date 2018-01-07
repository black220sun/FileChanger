package gui.util

import settings.Settings
import javax.swing.JMenu

open class LMenu(name: String) : JMenu(Settings.getLang(name))