package com.gitlab.lae.intellij.actions

import com.intellij.openapi.editor.CaretState
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class RectangleCreateTest : LightPlatformCodeInsightFixtureTestCase() {

  fun `test single line selection remains unchanged`() {
    test(
      text = "hello",
      initialSelections = listOf(selection(from(0, 1), to(0, 2))),
      expectedSelections = listOf(selection(from(0, 1, true), to(0, 2)))
    )
  }

  fun `test selects content in rectangle`() {
    // h[e]llo
    // w[o]rld
    test(
      text = "hello\nworld",
      initialSelections = listOf(
        selection(from(0, 1), to(1, 2))
      ),
      expectedSelections = listOf(
        selection(from(0, 1, true), to(0, 2)),
        selection(from(1, 1, true), to(1, 2))
      )
    )
  }

  fun `test selects short line in middle of rectangle`() {
    // h[ell]o
    // a[a]
    // w[orl]d
    test(
      text = "hello\naa\nworld",
      initialSelections = listOf(
        selection(from(0, 1), to(2, 4))
      ),
      expectedSelections = listOf(
        selection(from(0, 1, true), to(0, 4)),
        selection(from(1, 1, true), to(1, 2)),
        selection(from(2, 1, true), to(2, 4))
      )
    )
  }

  fun `test skips line if line has no content in rectangle`() {
    // h[ell]o
    // a
    // w[orl]d
    test(
      text = "hello\na\nworld",
      initialSelections = listOf(
        selection(from(0, 1), to(2, 4))
      ),
      expectedSelections = listOf(
        selection(from(0, 1, true), to(0, 4)),
        selection(from(2, 1, true), to(2, 4))
      )
    )
  }

  fun `test negative selection`() {
    // h[ell]o
    // a
    // w[orl]d
    test(
      text = "hello\na\nworld",
      initialSelections = listOf(
        selection(from(0, 4), to(2, 1))
      ),
      expectedSelections = listOf(
        CaretState(
          LogicalPosition(0, 4), // caret position
          LogicalPosition(0, 1), // from
          LogicalPosition(0, 4)  // to
        ),
        CaretState(
          LogicalPosition(2, 4), // caret position
          LogicalPosition(2, 1), // from
          LogicalPosition(2, 4)  // to
        )
      )
    )
  }

  fun `test works with multiple cursors`() {
    test(
      """
      hello world
      hi how
      are you today
      """.trimIndent(),
      initialSelections = listOf(
        selection(from(0, 1), to(1, 3)),
        selection(from(1, 5), to(2, 9))
      ),
      expectedSelections = listOf(
        selection(from(0, 1), to(0, 3)),
        selection(from(1, 1), to(1, 3)),
        selection(from(1, 5), to(1, 6)),
        selection(from(2, 5), to(2, 9))
      )
    )
  }

  private fun positions(carets: List<CaretState>): List<Triple<LogicalPosition, LogicalPosition, LogicalPosition>> =
    carets.map {
      Triple(
        it.caretPosition!!,
        it.selectionStart!!,
        it.selectionEnd!!
      )
    }

  private fun test(
    text: String,
    initialSelections: List<CaretState>,
    expectedSelections: List<CaretState>
  ) {
    myFixture.configureByText(PLAIN_TEXT, text)

    val caretModel = myFixture.editor.caretModel
    caretModel.caretsAndSelections = initialSelections

    myFixture.performEditorAction("com.gitlab.lae.intellij.actions.CreateRectangularSelectionFromMultiLineSelection")
    assertEquals(
      positions(expectedSelections),
      positions(caretModel.caretsAndSelections)
    )
  }

  private fun selection(start: LogicalPosition, end: LogicalPosition) =
    CaretState(start, start, end)

  private fun from(line: Int, column: Int, leanForward: Boolean = false) =
    LogicalPosition(line, column, leanForward)

  private fun to(line: Int, column: Int) = LogicalPosition(line, column)
}
