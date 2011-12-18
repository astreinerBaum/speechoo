/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.inputText;

/**
 *
 * @author Hugo Santos
 */
import com.sun.star.frame.XController;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XSentenceCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
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
        XTextViewCursor xCursor2 = xViewCursorSupplier.getViewCursor();
        
        XSentenceCursor xSC = (XSentenceCursor) UnoRuntime.queryInterface(
                XSentenceCursor.class, xCursor2);

        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xCursor2);

        xText.insertString(xCursor2, sentence, true);

        xCursor2.gotoRange(xCursor2.getEnd(), false);
        
        
    }
}
