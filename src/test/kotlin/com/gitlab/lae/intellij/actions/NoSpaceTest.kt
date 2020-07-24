package com.gitlab.lae.intellij.actions

import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

private const val NO_SPACE =
  "com.gitlab.lae.intellij.actions.NoSpace"

class NoSpaceTest : LightPlatformCodeInsightFixtureTestCase() {

  fun `test deletes all spaces and tabs`() {
    test("|HelloWorld", "HelloWorld")
    test("H|elloWorld", "HelloWorld")
    test("HelloWorld|", "HelloWorld")
    test("| HelloWorld", "HelloWorld")
    test("|  HelloWorld", "HelloWorld")
    test(" | HelloWorld", "HelloWorld")
    test("|\tHelloWorld", "HelloWorld")
    test("|\t\tHelloWorld", "HelloWorld")
    test("| \t \t  HelloWorld", "HelloWorld")
    test("Hello\t | \n World", "Hello\n World")
    test("Hello\t  \n| World", "Hello\t  \nWorld")
    test("Hello| World", "HelloWorld")
    test("Hello |World", "HelloWorld")
    test("Hello | World", "HelloWorld")
    test("Hello  |World", "HelloWorld")
  }

  private fun test(textAndCarets: String, expected: String) {
    val (text, carets) = parseTextSelections(textAndCarets)
    myFixture.configureByText(PLAIN_TEXT, text)
    myFixture.editor.caretModel.caretsAndSelections = carets
    myFixture.performEditorAction(NO_SPACE)
    assertEquals(expected, myFixture.editor.document.text)
  }
}
