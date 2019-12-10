package com.gitlab.lae.intellij.actions.simulation;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.intellij.openapi.actionSystem.PlatformDataKeys.CONTEXT_COMPONENT;
import static java.awt.event.KeyEvent.*;
import static java.lang.System.currentTimeMillis;

abstract class KeySimulation extends AnAction {

    private EventQueue eventQueue;

    private final int keyCode;

    KeySimulation(int keyCode) {
        this.keyCode = keyCode;
        setEnabledInModalContext(true);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Component component = event.getData(CONTEXT_COMPONENT);
        if (component != null) {
            if (eventQueue == null) {
                eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
            }
            eventQueue.postEvent(new KeyEvent(
                    component,
                    KEY_PRESSED,
                    currentTimeMillis(),
                    0,
                    keyCode,
                    CHAR_UNDEFINED,
                    KEY_LOCATION_STANDARD
            ));
        }
    }

}
