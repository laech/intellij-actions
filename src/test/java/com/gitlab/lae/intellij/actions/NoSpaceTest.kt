package com.gitlab.lae.intellij.actions

import com.intellij.openapi.fileTypes.FileTypes.PLAIN_TEXT
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

private const val NO_SPACE =
  "com.gitlab.lae.intellij.actions.NoSpace"

class NoSpaceTest : LightPlatformCodeInsightFixtureTestCase() {

  fun `test deletes all spaces and tabs`() {
    test("HelloWorld", "HelloWorld", 0)
    test("HelloWorld", "HelloWorld", 1)
    test("HelloWorld", "HelloWorld", 10)
    test(" HelloWorld", "HelloWorld", 0)
    test("  HelloWorld", "HelloWorld", 0)
    test("  HelloWorld", "HelloWorld", 1)
    test("\tHelloWorld", "HelloWorld", 0)
    test("\t\tHelloWorld", "HelloWorld", 0)
    test(" \t \t  HelloWorld", "HelloWorld", 0)
    test("Hello\t  \n World", "Hello\n World", 7)
    test("Hello\t  \n World", "Hello\t  \nWorld", 9)
    test("Hello World", "HelloWorld", 5)
    test("Hello World", "HelloWorld", 6)
    test("Hello  World", "HelloWorld", 6)
    test("Hello  World", "HelloWorld", 7)
  }

  private fun test(input: String, expected: String, caretOffset: Int) {
    myFixture.configureByText(PLAIN_TEXT, input)
    myFixture.editor.caretModel.moveToOffset(caretOffset)
    myFixture.performEditorAction(NO_SPACE)
    assertEquals(expected, myFixture.editor.document.text)
  }
}
