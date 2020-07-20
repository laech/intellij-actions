package com.gitlab.lae.intellij.actions

import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

private const val ONE_SPACE =
  "com.gitlab.lae.intellij.actions.OneSpace"

class OneSpaceTest : LightPlatformCodeInsightFixtureTestCase() {

  fun `test replaces all spaces and tabs with one space`() {
    test("HelloWorld", "HelloWorld", 0)
    test("HelloWorld", "HelloWorld", 1)
    test("HelloWorld", "HelloWorld", 10)
    test(" HelloWorld", " HelloWorld", 0)
    test("  HelloWorld", " HelloWorld", 0)
    test("  HelloWorld", " HelloWorld", 1)
    test("\tHelloWorld", " HelloWorld", 0)
    test("\t\tHelloWorld", " HelloWorld", 0)
    test(" \t \t  HelloWorld", " HelloWorld", 0)
    test("Hello\t  \n  World", "Hello \n  World", 7)
    test("Hello\t  \n  World", "Hello\t  \n World", 9)
    test("Hello  World", "Hello World", 5)
    test("Hello  World", "Hello World", 6)
    test("Hello  World", "Hello World", 7)
  }

  private fun test(input: String, expected: String, caretOffset: Int) {
    myFixture.configureByText(PLAIN_TEXT, input)
    myFixture.editor.caretModel.moveToOffset(caretOffset)
    myFixture.performEditorAction(ONE_SPACE)
    assertEquals(expected, myFixture.editor.document.text)
  }
}
