package com.gitlab.lae.intellij.actions

import com.intellij.openapi.editor.CaretState
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class RectangleCreateTest : LightPlatformCodeInsightFixtureTestCase() {

  fun `test single line selection remains unchanged`() {
    test(
      "h[e]llo",
      "h[e]llo"
    )
  }

  fun `test selects content in rectangle`() {
    test(
      """
        h[ello
        wo]rld
      """,
      """
        h[e]llo
        w[o]rld
      """
    )
  }

  fun `test selects short line in middle of rectangle`() {
    test(
      """
        h[ello
        aa
        worl]d
      """,
      """
        h[ell]o
        a[a]
        w[orl]d
      """
    )
  }

  fun `test skips line if line has no content in rectangle`() {
    test(
      """
        h[ello
        a
        worl]d
      """,
      """
        h[ell]o
        a
        w[orl]d
      """
    )
  }

  fun `test negative selection`() {
    test(
      """
        hell[o
        a
        w]orld
      """,
      """
        h[ell]o
        a
        w[orl]d
      """,
      true
    )
  }

  fun `test works with multiple cursors`() {
    test(
      """
        h[ello world
        hi ]ho[w
        are you t]oday
      """,
      """
        h[el]lo world
        h[i ]ho[w]
        are y[ou t]oday
      """
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
    initialTextAndSelections: String,
    expectedTextAndSelections: String,
    expectedCaretPositionAtSelectionEnd: Boolean = false
  ) {
    val (initialText, initialSelections) = parseTextSelections(
      initialTextAndSelections.trimIndent()
    )
    val (expectedText, expectedSelections) = parseTextSelections(
      expectedTextAndSelections.trimIndent(),
      expectedCaretPositionAtSelectionEnd
    )
    assertEquals(initialText, expectedText)

    myFixture.configureByText(PLAIN_TEXT, initialText)

    val caretModel = myFixture.editor.caretModel
    caretModel.caretsAndSelections = initialSelections

    myFixture.performEditorAction("com.gitlab.lae.intellij.actions.CreateRectangularSelectionFromMultiLineSelection")
    assertEquals(
      positions(expectedSelections),
      positions(caretModel.caretsAndSelections)
    )
  }

}
