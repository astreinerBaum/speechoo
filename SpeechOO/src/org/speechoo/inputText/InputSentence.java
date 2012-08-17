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
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.SpeechOO;
import org.speechoo.gui.InputDevicesControl;
import org.speechoo.util.CoGrOO;
import org.speechoo.util.PrintAndSave;


public class InputSentence {

PostProcessor postProcessor = new PostProcessor();
public static XTextRange xRange;

public static XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,
                SpeechOO.m_xFrame.getController().getModel());

    public void insertNewSentence(String sentence, int length) {
        //System.out.println("insertNewSentence");
        XText xText = xDoc.getText();
        XController xController = xDoc.getCurrentController();
        XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(
        XTextViewCursorSupplier.class, xController);
        XTextViewCursor xCursor = xViewCursorSupplier.getViewCursor();
        
        XSentenceCursor xSC = (XSentenceCursor) UnoRuntime.queryInterface(
                XSentenceCursor.class, xCursor);

        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xCursor);
        if(PrintAndSave.flag==1){
            try {
                InputDevicesControl.keyEnter();
            } catch (Exception ex) {
                Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
            }
        PrintAndSave.saveDocument(sentence);
        PrintAndSave.flag=0;
        SpeechOO.dic.setEnabled(false);
        SpeechOO.gram.setEnabled(true);
        }
        else{
        xRange = xCursor.getStart();
        CoGrOO.main(sentence, length);
        xText.insertString(xCursor, sentence + " ", true);
        xCursor.gotoRange(xCursor.getEnd(), false);
        }
}
}
