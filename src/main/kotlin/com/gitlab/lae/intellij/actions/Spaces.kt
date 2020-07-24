package com.gitlab.lae.intellij.actions

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor

internal class NSpace(private val count: Int) : Edit {

  init {
    require(count >= 0) { "count=$count" }
  }

  override fun invoke(editor: Editor, caret: Caret, context: DataContext?) {
    val doc = editor.document
    val chars = doc.immutableCharSequence
    val offset = caret.offset
    if (offset >= chars.length) {
      return
    }

    val spaceStart = (offset downTo 1)
      .firstOrNull { !chars[it - 1].isSpaceOrTab() } ?: 0

    val spaceEnd = (offset until chars.length)
      .firstOrNull { !chars[it].isSpaceOrTab() } ?: chars.length

    if (spaceStart < spaceEnd) {
      doc.replaceString(spaceStart, spaceEnd, " ".repeat(count))
    }
  }

  private fun Char.isSpaceOrTab() =
    this == ' ' || this == '\t'

}

class NoSpace : TextAction(true, NSpace(0))
class OneSpace : TextAction(true, NSpace(1))
