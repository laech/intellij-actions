package com.gitlab.lae.intellij.actions.simulation;

import javax.swing.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Tmp {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("keyTyped " + (int) e.getKeyChar() + " " + e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed " + e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("keyReleased " + e);
            }
        });
        frame.setVisible(true);
    }
}
