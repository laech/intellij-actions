package com.gitlab.lae.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.awt.*;
import java.awt.event.KeyEvent;

abstract class KeyAction extends AnAction {

    private static Robot robot = null;

    private static Robot initRobot() {
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
        return robot;
    }

    private final int keyCode;

    KeyAction(int keyCode) {
        this.keyCode = keyCode;
        setEnabledInModalContext(true);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Robot robot = initRobot();
        if (robot != null) {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }
    }
}
