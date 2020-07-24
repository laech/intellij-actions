package com.gitlab.lae.intellij.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR
import com.intellij.openapi.actionSystem.ToggleAction

class CamelHumpsInCurrentEditor : ToggleAction() {

  init {
    isEnabledInModalContext = true
  }

  override fun isSelected(event: AnActionEvent) =
    event.getData(EDITOR)?.settings?.isCamelWords ?: false

  override fun setSelected(event: AnActionEvent, state: Boolean) {
    event.getData(EDITOR)?.settings?.isCamelWords = true
  }
}
