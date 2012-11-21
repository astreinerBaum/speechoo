/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.util;

/**
 *
 * @author Welton Araújo
 */

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
public class InputDevicesControl {
protected static KeyEvent a;


public static void backSpace() throws Exception {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
}
 public static void keyDown() throws Exception{
     Robot robot = new Robot();
     robot.keyPress(KeyEvent.VK_DOWN);
    }
 public static void leftClick() throws Exception {
        Robot robot = new Robot();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
public static void keyEnter() throws Exception{
     Robot robot = new Robot();
     robot.keyPress(KeyEvent.VK_ENTER);
     robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
