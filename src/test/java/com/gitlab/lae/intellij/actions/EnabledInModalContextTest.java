package com.gitlab.lae.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public final class EnabledInModalContextTest {

    @Parameters(name = "{0}")
    public static Object[][] parameters() {
        return new Object[][]{
                {new CamelHumpsInCurrentEditor()},
                {new CapitalizeRegionOrToWordEnd()},
                {new Down()},
                {new DowncaseRegionOrToWordEnd()},
                {new Escape()},
                {new NoSpace()},
                {new OneSpace()},
                {new RectangleCreate()},
                {new Up()},
                {new UpcaseRegionOrToWordEnd()},
        };
    }

    private final AnAction action;

    public EnabledInModalContextTest(AnAction action) {
        this.action = requireNonNull(action);
    }

    @Test
    public void enabledInModalContext() {
        assertTrue(action.isEnabledInModalContext());
    }
}
