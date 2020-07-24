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
    test("|HELLO WORLD", "HELLO WORLD", UPCASE)
    test("|HELLO WORLD", "HELLO WORLD", UPCASE, 2)
    test("|hello world", "HELLO world", UPCASE)
    test("|hello world", "HELLO WORLD", UPCASE, 2)
    test("h|ello world", "hELLO world", UPCASE)
    test("h|ello world", "hELLO WORLD", UPCASE, 2)
    test("hello worl|d", "hello worlD", UPCASE)
    test("hello world|", "hello world", UPCASE)
    test("hello world|", "hello world", UPCASE, 2)
    test("hello| world", "hello WORLD", UPCASE)
    test("hello |world", "hello WORLD", UPCASE)
    test("hello| ;; world", "hello ;; WORLD", UPCASE)
    test("hello |;; world", "hello ;; WORLD", UPCASE)
    test("|hello\nworld", "HELLO\nworld", UPCASE)
    test("|hello\nworld", "HELLO\nWORLD", UPCASE, 2)
    test("|hello-world", "HELLO-world", UPCASE)
    test("|hello-world", "HELLO-WORLD", UPCASE, 2)
  }

  fun `test upcase region`() {
    test("[HELLO WORLD]", "HELLO WORLD", UPCASE)
    test("[HELLO WORLD]", "HELLO WORLD", UPCASE, 2)
    test("[hello] world", "HELLO world", UPCASE)
    test("h[ello] world", "hELLO world", UPCASE)
    test("h[ello world]", "hELLO WORLD", UPCASE)
    test("hello worl[d]", "hello worlD", UPCASE)
    test("hello[ world]", "hello WORLD", UPCASE)
    test("hello [world]", "hello WORLD", UPCASE)
    test("hello[ ;; \nworld]", "hello ;; \nWORLD", UPCASE)
    test("hello [;; world]", "hello ;; WORLD", UPCASE)
  }

  fun `test downcase to word end`() {
    test("|HELLO WORLD", "hello WORLD", DOWNCASE)
    test("HE|LLO WORLD", "HEllo WORLD", DOWNCASE)
    test("|HELLO WORLD", "hello world", DOWNCASE, 2)
  }

  fun `test downcase region`() {
    test("[HELLO W]ORLD", "hello wORLD", DOWNCASE)
  }

  fun `test capitalize to word end`() {
    test("|Hello", "Hello", CAPITALIZE)
    test("|HELLO WORLD", "Hello WORLD", CAPITALIZE)
    test("|HELLO WORLD", "Hello World", CAPITALIZE, 2)
    test("h|ello-world", "hEllo-world", CAPITALIZE)
    test("|hello-world", "Hello-World", CAPITALIZE, 2)
  }

  fun `test capitalize region`() {
    test("[HELLO] WORLD", "Hello WORLD", CAPITALIZE)
    test("HE[LLO WOR]LD", "HELlo WorLD", CAPITALIZE)
    test("he[llo-WOR]LD", "heLlo-WorLD", CAPITALIZE)
  }

  private fun test(
    initialTextAndSelections: String,
    expectedText: String,
    actionId: String,
    times: Int = 1
  ) {
    val (initialText, initialSelections) = parseTextSelections(
      initialTextAndSelections
    )
    myFixture.configureByText(PLAIN_TEXT, initialText)
    myFixture.editor.caretModel.caretsAndSelections = initialSelections

    repeat(times) { myFixture.performEditorAction(actionId) }
    assertEquals(expectedText, myFixture.editor.document.text)
  }
}
