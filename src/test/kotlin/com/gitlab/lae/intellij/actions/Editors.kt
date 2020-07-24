package com.gitlab.lae.intellij.actions

import com.intellij.openapi.editor.CaretState
import com.intellij.openapi.editor.LogicalPosition

/**
 * Parses a specially formatted string containing selections:
 *  - "[" indicates a selection start
 *  - "]" indicates a selection end
 *  - "|" short for "[]", i.e. just caret, no selection
 *
 * Returns a string with the above characters removed, and the selections.
 */
fun parseTextSelections(
  textAndSelections: String,
  caretPositionAtSelectionEnd: Boolean = false
): Pair<String, List<CaretState>> =
  textAndSelections.replace("[\\[\\]|]".toRegex(), "") to
    textAndSelections.replace("|", "[]")
      .lineSequence()
      .withIndex()
      .flatMap { (line, str) ->
        str
          .asSequence()
          .mapIndexedNotNull { column, char ->
            when (char) {
              '[', ']' -> (column to char)
              else -> null
            }
          }
          .mapIndexed { i, (column, char) -> Triple(line, column - i, char) }
      }
      .chunked(2)
      .map { (start, end) ->
        val (startLine, startColumn, startChar) = start
        val (endLine, endColumn, endChar) = end
        require(startChar == '[')
        require(endChar == ']')

        val selectionStart = LogicalPosition(startLine, startColumn)
        val selectionEnd = LogicalPosition(endLine, endColumn)
        val caretPosition =
          if (caretPositionAtSelectionEnd) selectionEnd
          else selectionStart
        CaretState(caretPosition, selectionStart, selectionEnd)
      }
      .toList()
