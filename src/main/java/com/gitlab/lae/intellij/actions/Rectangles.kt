package com.gitlab.lae.intellij.actions

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_DELETE
import com.intellij.openapi.editor.*
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.editor.actions.TextComponentEditorAction

sealed class RectangleAction(val action: (Editor, Caret?, DataContext) -> Unit) :
  TextComponentEditorAction(object : EditorActionHandler(false) {
    override fun doExecute(
      editor: Editor,
      caret: Caret?,
      context: DataContext
    ) {
      val caretModel = editor.caretModel
      if (!caretModel.supportsMultipleCarets()) {
        return
      }
      caretModel.caretsAndSelections =
        caretModel.caretsAndSelections
          .flatMap { toRectangle(it, editor.document) }
      action(editor, caret, context)
    }
  })

class RectangleCreate : RectangleAction({ _, _, _ -> })

class RectangleDelete : RectangleAction({ editor, caret, context ->
  EditorActionManager
    .getInstance()
    .getActionHandler(ACTION_EDITOR_DELETE)
    .execute(editor, caret, context)
})

private fun toRectangle(cs: CaretState, doc: Document): List<CaretState> {
  val selectionEnd = cs.selectionEnd ?: return emptyList()
  val selectionStart = cs.selectionStart ?: return emptyList()
  return if (selectionStart.line == selectionEnd.line) {
    listOf(cs)
  } else {
    (selectionStart.line..selectionEnd.line)
      .filter { hasEnoughColumns(selectionStart, selectionEnd, it, doc) }
      .map { toSelection(selectionStart, selectionEnd, it) }
  }
}

private fun hasEnoughColumns(
  selectionStart: LogicalPosition,
  selectionEnd: LogicalPosition,
  line: Int,
  doc: Document
): Boolean {
  val columns = (doc.getLineEndOffset(line) - doc.getLineStartOffset(line))
  return (selectionStart.column < columns || selectionEnd.column < columns)
}

private fun toSelection(
  selectionStart: LogicalPosition,
  selectionEnd: LogicalPosition,
  line: Int
): CaretState {
  val lineSelectionStart = LogicalPosition(line, selectionStart.column)
  val lineSelectionEnd = LogicalPosition(line, selectionEnd.column)
  return CaretState(
    lineSelectionStart,
    lineSelectionStart,
    lineSelectionEnd
  )
}
