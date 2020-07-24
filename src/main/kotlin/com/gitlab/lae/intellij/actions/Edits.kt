package com.gitlab.lae.intellij.actions

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler
import com.intellij.openapi.editor.actions.TextComponentEditorAction
import com.intellij.openapi.util.TextRange
import java.lang.Character.isLowerCase
import java.lang.Character.isUpperCase
import kotlin.math.max
import kotlin.math.min

typealias Edit = (Editor, Caret, DataContext?) -> Unit

fun Edit.toHandler(): EditorActionHandler =
  object : EditorActionHandler(true) {
    override fun doExecute(
      editor: Editor,
      caret: Caret?,
      context: DataContext?
    ) {
      if (caret != null && caret.isValid) {
        invoke(editor, caret, context)
      }
    }
  }

fun Edit.toWriteHandler(): EditorWriteActionHandler =
  object : EditorWriteActionHandler(true) {
    override fun executeWriteAction(
      editor: Editor,
      caret: Caret?,
      context: DataContext?
    ) {
      if (caret != null && caret.isValid) {
        invoke(editor, caret, context)
      }
    }
  }

fun Edit.ifNoSelection(other: Edit): Edit = { editor, caret, context ->
  (if (caret.hasSelection()) this else other)(editor, caret, context)
}

fun replacingSelection(replace: (String) -> String): Edit =
  f@{ editor, caret, _ ->
    if (!caret.hasSelection()) {
      return@f
    }
    val start = caret.selectionStart
    val end = caret.selectionEnd
    val doc = editor.document
    val replacement = replace(doc.getText(TextRange(start, end)))
    caret.removeSelection()
    doc.replaceString(start, end, replacement)
    caret.setSelection(start, start + replacement.length)
  }

fun replacingFromCaret(id: String, replace: (String) -> String): Edit =
  f@{ editor, caret, context ->
    caret.removeSelection()
    val (start, end) = moveAndGetRegion(id, editor, caret, context)
    if (start == end) {
      return@f
    }
    val doc = editor.document
    val text = doc.getText(TextRange(start, end))
    val replacement = replace(text)
    doc.replaceString(start, end, replacement)
  }

private fun moveAndGetRegion(
  actionId: String,
  editor: Editor,
  caret: Caret,
  context: DataContext?
): Pair<Int, Int> {
  val offset1 = caret.offset
  var offset2 = offset1
  while (offset2 < editor.document.textLength) {
    offset2 = executeMoveAction(actionId, editor, caret, context)
    if (editor.document.charsSequence
        .subSequence(min(offset1, offset2), max(offset1, offset2))
        .codePoints()
        .anyMatch { isUpperCase(it) != isLowerCase(it) }
    ) {
      break
    }
  }
  return min(offset1, offset2) to max(offset1, offset2)
}

private fun executeMoveAction(
  actionId: String,
  editor: Editor,
  caret: Caret,
  context: DataContext?
): Int {
  EditorActionManager
    .getInstance()
    .getActionHandler(actionId)
    .execute(editor, caret, context)
  return caret.offset
}

open class TextAction(write: Boolean, edit: Edit) :
  TextComponentEditorAction(
    if (write) edit.toWriteHandler()
    else edit.toHandler()
  )
