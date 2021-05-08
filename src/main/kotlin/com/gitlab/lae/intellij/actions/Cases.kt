package com.gitlab.lae.intellij.actions

import com.intellij.openapi.actionSystem.IdeActions.ACTION_EDITOR_NEXT_WORD
import java.util.regex.Pattern

private val upcaseRegion =
  replacingSelection(String::uppercase)

private val upcaseToWordEnd =
  replacingFromCaret(ACTION_EDITOR_NEXT_WORD, String::uppercase)

class UpcaseRegionOrToWordEnd :
  TextAction(true, upcaseRegion.ifNoSelection(upcaseToWordEnd))


private val downcaseRegion =
  replacingSelection(String::lowercase)

private val downcaseToWordEnd =
  replacingFromCaret(ACTION_EDITOR_NEXT_WORD, String::lowercase)

class DowncaseRegionOrToWordEnd :
  TextAction(true, downcaseRegion.ifNoSelection(downcaseToWordEnd))


private val capitalizeRegion =
  replacingSelection(::capitalize)

private val capitalizeToWordEnd =
  replacingFromCaret(ACTION_EDITOR_NEXT_WORD, ::capitalize)

private val wordPattern = Pattern.compile("\\w+")

private fun capitalize(str: String): String {
  val buffer = StringBuffer(str.length)
  val matcher = wordPattern.matcher(str)
  while (matcher.find()) {
    val capitalized =
      matcher.group().lowercase().replaceFirstChar(Char::titlecase)
    matcher.appendReplacement(buffer, capitalized)
  }
  return matcher.appendTail(buffer).toString()
}

class CapitalizeRegionOrToWordEnd :
  TextAction(true, capitalizeRegion.ifNoSelection(capitalizeToWordEnd))
