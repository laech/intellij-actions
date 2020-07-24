package com.gitlab.lae.intellij.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys.CONTEXT_COMPONENT
import java.awt.EventQueue
import java.awt.Toolkit.getDefaultToolkit
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.lang.System.currentTimeMillis

abstract class KeySimulations(private val keyCode: Int) : AnAction() {

  private var eventQueue: EventQueue? = null

  init {
    isEnabledInModalContext = true
  }

  override fun actionPerformed(event: AnActionEvent) {
    val component = event.getData(CONTEXT_COMPONENT)
    if (component != null) {
      if (eventQueue == null) {
        eventQueue = getDefaultToolkit().systemEventQueue
      }
      eventQueue!!.postEvent(
        KeyEvent(
          component,
          KEY_PRESSED,
          currentTimeMillis(),
          0,
          keyCode,
          CHAR_UNDEFINED,
          KEY_LOCATION_STANDARD
        )
      )
    }
  }
}

class Up : KeySimulations(VK_UP)
class Down : KeySimulations(VK_DOWN)
class Enter : KeySimulations(VK_ENTER)
class Escape : KeySimulations(VK_ESCAPE)
