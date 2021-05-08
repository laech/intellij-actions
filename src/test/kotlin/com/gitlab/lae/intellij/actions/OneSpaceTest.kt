package com.gitlab.lae.intellij.actions

import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.BasePlatformTestCase

private const val ONE_SPACE =
  "com.gitlab.lae.intellij.actions.OneSpace"

class OneSpaceTest : BasePlatformTestCase() {

  fun `test replaces all spaces and tabs with one space`() {
    test("|HelloWorld", "HelloWorld")
    test("H|elloWorld", "HelloWorld")
    test("HelloWorld|", "HelloWorld")
    test("| HelloWorld", " HelloWorld")
    test("|  HelloWorld", " HelloWorld")
    test(" | HelloWorld", " HelloWorld")
    test("|\tHelloWorld", " HelloWorld")
    test("|\t\tHelloWorld", " HelloWorld")
    test("| \t \t  HelloWorld", " HelloWorld")
    test("Hello\t | \n  World", "Hello \n  World")
    test("Hello\t  \n|  World", "Hello\t  \n World")
    test("Hello|  World", "Hello World")
    test("Hello | World", "Hello World")
    test("Hello  |World", "Hello World")
  }

  private fun test(textAndCarets: String, expected: String) {
    val (text, carets) = parseTextSelections(textAndCarets)
    myFixture.configureByText(PLAIN_TEXT, text)
    myFixture.editor.caretModel.caretsAndSelections = carets
    myFixture.performEditorAction(ONE_SPACE)
    assertEquals(expected, myFixture.editor.document.text)
  }
}
