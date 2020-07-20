package com.gitlab.lae.intellij.actions

import com.gitlab.lae.intellij.actions.simulation.Down
import com.gitlab.lae.intellij.actions.simulation.Enter
import com.gitlab.lae.intellij.actions.simulation.Escape
import com.gitlab.lae.intellij.actions.simulation.Up
import com.intellij.openapi.actionSystem.AnAction
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class EnabledInModalContextTest(private val action: AnAction) {

  @Test
  fun enabledInModalContext() {
    assertTrue(action.isEnabledInModalContext)
  }

  companion object {
    @JvmStatic
    @Parameters(name = "{0}")
    fun parameters() = arrayOf(
      arrayOf<Any>(Down()),
      arrayOf<Any>(Enter()),
      arrayOf<Any>(Escape()),
      arrayOf<Any>(Up()),
      arrayOf<Any>(CamelHumpsInCurrentEditor()),
      arrayOf<Any>(CapitalizeRegionOrToWordEnd()),
      arrayOf<Any>(DowncaseRegionOrToWordEnd()),
      arrayOf<Any>(NoSpace()),
      arrayOf<Any>(OneSpace()),
      arrayOf<Any>(RectangleCreate()),
      arrayOf<Any>(UpcaseRegionOrToWordEnd())
    )
  }
}
