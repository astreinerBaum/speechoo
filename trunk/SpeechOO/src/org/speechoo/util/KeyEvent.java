/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.util;

/**
 *
 * @author Welton Araújo
 */
import com.sun.star.awt.XExtendedToolkit;
import com.sun.star.awt.XKeyHandler;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.uno.UnoRuntime;
import org.speechoo.SpeechOO;

public class KeyEvent {

    public static void begin() {
        XWindow xWindow = SpeechOO.m_xFrame.getComponentWindow();
        XWindowPeer MyWindowPeer = (XWindowPeer) UnoRuntime.queryInterface(XWindowPeer.class, xWindow);
        XToolkit MyToolkit = MyWindowPeer.getToolkit();
        XExtendedToolkit MyExtToolkit = (XExtendedToolkit) UnoRuntime.queryInterface(XExtendedToolkit.class, MyToolkit);
        MyExtToolkit.addKeyHandler(new XKeyHandler() {

            public boolean keyPressed(com.sun.star.awt.KeyEvent arg0) {
                if (arg0.KeyCode == 0) {
                    System.out.println("dic paused");
                    SpeechOO.dic.setEnabled(false);
                    System.out.println("gram resumed");
                    SpeechOO.gram.setEnabled(true);
                    SpeechOO.label.setText("Modo Comando Ativado");
                    SpeechOO.label.setVisible(true);
                }
                return false;
            }

            public boolean keyReleased(com.sun.star.awt.KeyEvent arg0) {
                if (arg0.KeyCode == 0) {
                    System.out.println("dic resumed");
                    SpeechOO.dic.setEnabled(true);
                    System.out.println("gram paused");
                    SpeechOO.gram.setEnabled(false);
                    SpeechOO.label.setText("Modo Ditado Ativado");
                    SpeechOO.label.setVisible(true);
                }
                return false;
            }

            public void disposing(com.sun.star.lang.EventObject arg0) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
}
