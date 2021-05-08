package com.gitlab.lae.intellij.actions

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.CaretState
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class RectangleCreateTest(
  private val initialTextAndSelections: String,
  private val expectedTextAndSelections: String,
  private val expectedCaretPositionAtSelectionEnd: Boolean
) : BasePlatformTestCase() {

  companion object {
    @JvmStatic
    @Parameters(name = "{index}: {0} -> {1}")
    fun params() = arrayOf(
      arrayOf(
        "h[e]llo",
        "h[e]llo",
        false
      ),
      arrayOf(
        """
          h[ello
          wo]rld
        """,
        """
          h[e]llo
          w[o]rld
        """,
        false
      ),
      arrayOf(
        """
          h[ello
          aa
          worl]d
        """,
        """
          h[ell]o
          a[a]
          w[orl]d
        """,
        false
      ),
      arrayOf(
        """
          h[ello
          a
          worl]d
        """,
        """
          h[ell]o
          a
          w[orl]d
        """,
        false
      ),
      arrayOf(
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
      ),
      arrayOf(
        """
          h[ello world
          hi ]ho[w
          are you t]oday
        """,
        """
          h[el]lo world
          h[i ]ho[w]
          are y[ou t]oday
        """,
        false
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

  @Test
  fun test() {
    val (initialText, initialSelections) = parseTextSelections(
      initialTextAndSelections.trimIndent()
    )
    val (expectedText, expectedSelections) = parseTextSelections(
      expectedTextAndSelections.trimIndent(),
      expectedCaretPositionAtSelectionEnd
    )
    assertEquals(initialText, expectedText)

    ApplicationManager.getApplication().invokeAndWait {
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
}
