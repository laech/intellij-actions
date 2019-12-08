package com.gitlab.lae.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.intellij.openapi.actionSystem.PlatformDataKeys.CONTEXT_COMPONENT;
import static java.awt.event.KeyEvent.CHAR_UNDEFINED;
import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.lang.System.currentTimeMillis;

abstract class KeyAction extends AnAction {

    private final int keyCode;

    KeyAction(int keyCode) {
        this.keyCode = keyCode;
        setEnabledInModalContext(true);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Component component = event.getData(CONTEXT_COMPONENT);
        if (component != null) {
            component.dispatchEvent(newKeyEvent(component));
        }
    }

    private KeyEvent newKeyEvent(Component component) {
        return new KeyEvent(
                component,
                KEY_PRESSED,
                currentTimeMillis(),
                0,
                keyCode,
                CHAR_UNDEFINED
        );
    }
}
