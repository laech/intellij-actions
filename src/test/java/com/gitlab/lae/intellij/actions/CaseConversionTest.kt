package com.gitlab.lae.intellij.actions

import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

private const val UPCASE =
  "com.gitlab.lae.intellij.actions.UpcaseRegionOrToWordEnd"

private const val DOWNCASE =
  "com.gitlab.lae.intellij.actions.DowncaseRegionOrToWordEnd"

private const val CAPITALIZE =
  "com.gitlab.lae.intellij.actions.CapitalizeRegionOrToWordEnd"

class CaseConversionTest : LightPlatformCodeInsightFixtureTestCase() {

  fun `test upcase to word end`() {
    test("HELLO WORLD", "HELLO WORLD", 0, UPCASE)
    test("HELLO WORLD", "HELLO WORLD", 0, UPCASE, 2)
    test("hello world", "HELLO world", 0, UPCASE)
    test("hello world", "HELLO WORLD", 0, UPCASE, 2)
    test("hello world", "hELLO world", 1, UPCASE)
    test("hello world", "hELLO WORLD", 1, UPCASE, 2)
    test("hello world", "hello worlD", 10, UPCASE)
    test("hello world", "hello world", 11, UPCASE)
    test("hello world", "hello world", 11, UPCASE, 2)
    test("hello world", "hello WORLD", 5, UPCASE)
    test("hello world", "hello WORLD", 6, UPCASE)
    test("hello ;; world", "hello ;; WORLD", 5, UPCASE)
    test("hello ;; world", "hello ;; WORLD", 6, UPCASE)
    test("hello\nworld", "HELLO\nworld", 0, UPCASE)
    test("hello\nworld", "HELLO\nWORLD", 0, UPCASE, 2)
    test("hello-world", "HELLO-world", 0, UPCASE)
    test("hello-world", "HELLO-WORLD", 0, UPCASE, 2)
  }

  fun `test upcase region`() {
    test("HELLO WORLD", "HELLO WORLD", 0, 11, UPCASE, 1)
    test("HELLO WORLD", "HELLO WORLD", 0, 11, UPCASE, 2)
    test("hello world", "HELLO world", 0, 5, UPCASE, 1)
    test("hello world", "hELLO world", 1, 5, UPCASE, 1)
    test("hello world", "hELLO WORLD", 1, 11, UPCASE, 1)
    test("hello world", "hello worlD", 10, 11, UPCASE, 1)
    test("hello world", "hello WORLD", 5, 11, UPCASE, 1)
    test("hello world", "hello WORLD", 6, 11, UPCASE, 1)
    test("hello ;; \nworld", "hello ;; \nWORLD", 5, 15, UPCASE, 1)
    test("hello ;; world", "hello ;; WORLD", 6, 14, UPCASE, 1)
  }

  fun `test downcase to word end`() {
    test("HELLO WORLD", "hello WORLD", 0, DOWNCASE)
    test("HELLO WORLD", "HEllo WORLD", 2, DOWNCASE)
    test("HELLO WORLD", "hello world", 0, DOWNCASE, 2)
  }

  fun `test downcase region`() {
    test("HELLO WORLD", "hello wORLD", 0, 7, DOWNCASE, 1)
  }

  fun `test capitalize to word end`() {
    test("Hello", "Hello", 0, CAPITALIZE)
    test("HELLO WORLD", "Hello WORLD", 0, CAPITALIZE)
    test("HELLO WORLD", "Hello World", 0, CAPITALIZE, 2)
    test("hello-world", "hEllo-world", 1, CAPITALIZE)
    test("hello-world", "Hello-World", 0, CAPITALIZE, 2)
  }

  fun `test capitalize region`() {
    test("HELLO WORLD", "Hello WORLD", 0, 5, CAPITALIZE, 1)
    test("HELLO WORLD", "HELlo WorLD", 2, 9, CAPITALIZE, 1)
    test("hello-WORLD", "heLlo-WorLD", 2, 9, CAPITALIZE, 1)
  }

  private fun test(
    initialText: String,
    expectedText: String,
    caretOffset: Int,
    actionId: String,
    times: Int = 1
  ) {
    test(
      initialText,
      expectedText,
      caretOffset,
      caretOffset,
      actionId,
      times
    )
  }

  private fun test(
    initialText: String,
    expectedText: String,
    selectionStart: Int,
    selectionEnd: Int,
    actionId: String,
    times: Int
  ) {
    myFixture.configureByText(PLAIN_TEXT, initialText)

    val caretModel = myFixture.editor.caretModel
    caretModel.moveToOffset(selectionStart)
    caretModel.primaryCaret.setSelection(selectionStart, selectionEnd)

    repeat(times) { myFixture.performEditorAction(actionId) }
    assertEquals(expectedText, myFixture.editor.document.text)
  }
}
