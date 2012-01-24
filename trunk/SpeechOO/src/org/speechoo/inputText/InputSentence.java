/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.inputText;

/**
 *
 * @author Hugo Santos
 */
import com.sun.star.awt.XWindow;
import com.sun.star.frame.XController;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XFrameActionListener;
import com.sun.star.frame.XFramesSupplier;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XEventListener;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XSentenceCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import java.awt.event.KeyEvent;
import org.speechoo.SpeechOO;


public class InputSentence {

    PostProcessor postProcessor = new PostProcessor();




    public void insertNewSentence(String sentence) {
        //System.out.println("insertNewSentence");

        XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,
                SpeechOO.m_xFrame.getController().getModel());
        XText xText = xDoc.getText();
        XController xController = xDoc.getCurrentController();
        XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(
        XTextViewCursorSupplier.class, xController);
        XTextViewCursor xCursor = xViewCursorSupplier.getViewCursor();
        
        XSentenceCursor xSC = (XSentenceCursor) UnoRuntime.queryInterface(
                XSentenceCursor.class, xCursor);

        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xCursor);

        xText.insertString(xCursor, sentence, true);

        xCursor.gotoRange(xCursor.getEnd(), false);
        
    }

    public void keyTyped(KeyEvent e) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            System.out.println("dic paused");
            SpeechOO.dic.setEnabled(false);
            System.out.println("gram resumed");
            SpeechOO.gram.setEnabled(true);

            }
        //  displayInfo(e, "KEY PRESSED: ");

        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("dic resumed");
        SpeechOO.dic.setEnabled(true);
        System.out.println("gram paused");
        SpeechOO.gram.setEnabled(false);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyPressed(com.sun.star.awt.KeyEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(com.sun.star.awt.KeyEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void disposing(EventObject arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void initialize(XWindow arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XWindow getContainerWindow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCreator(XFramesSupplier arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XFramesSupplier getCreator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XFrame findFrame(String arg0, int arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void activate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deactivate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setComponent(XWindow arg0, XController arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XWindow getComponentWindow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XController getController() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void contextChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addFrameActionListener(XFrameActionListener arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeFrameActionListener(XFrameActionListener arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addEventListener(XEventListener arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeEventListener(XEventListener arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
