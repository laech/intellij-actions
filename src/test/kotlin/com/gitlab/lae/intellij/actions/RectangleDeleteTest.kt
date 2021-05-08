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
class RectangleDeleteTest(
  private val initialTextAndSelections: String,
  private val expectedTextAndSelections: String,
  private val expectedCaretPositionAtSelectionEnd: Boolean
) : BasePlatformTestCase() {

  companion object {
    @JvmStatic
    @Parameters(name = "{index}: {0} -> {1}")
    fun parmeters() = arrayOf(
      arrayOf(
        "h[e]llo",
        "h|llo",
        false
      ),
      arrayOf(
        """
          h[ello
          wo]rld
        """,
        """
          h|llo
          w|rld
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
          h|o
          a|
          w|d
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
          h|o
          a
          w|d
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
          h|o
          a
          w|d
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
          h|lo world
          h|ho|
          are y|oday
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

    ApplicationManager.getApplication().invokeAndWait {
      myFixture.configureByText(PLAIN_TEXT, initialText)
      myFixture.editor.caretModel.caretsAndSelections = initialSelections

      myFixture.performEditorAction("com.gitlab.lae.intellij.actions.RectangleDelete")
      assertEquals(expectedText, myFixture.editor.document.text)
      assertEquals(
        positions(expectedSelections),
        positions(myFixture.editor.caretModel.caretsAndSelections)
      )
    }
  }
}
