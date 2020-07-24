package com.gitlab.lae.intellij.actions

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
      arrayOf(Down()),
      arrayOf(Enter()),
      arrayOf(Escape()),
      arrayOf(Up()),
      arrayOf(CamelHumpsInCurrentEditor()),
      arrayOf(CapitalizeRegionOrToWordEnd()),
      arrayOf(DowncaseRegionOrToWordEnd()),
      arrayOf(NoSpace()),
      arrayOf(OneSpace()),
      arrayOf(RectangleCreate()),
      arrayOf(UpcaseRegionOrToWordEnd())
    )
  }
}
