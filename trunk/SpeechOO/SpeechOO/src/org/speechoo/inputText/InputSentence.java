/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.inputText;

/**
 *
 * @author 10080000701
 */

import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import org.speechoo.SpeechOO;

public class InputSentence {

    PostProcessor postProcessor = new PostProcessor();

    public void insertNewSentence(String sentence) {
        System.out.println("insertNewSentence");

        postProcessor.processUtterance(sentence);
        XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,
                SpeechOO.m_xFrame.getController().getModel());
        XText xText = xDoc.getText();
        XTextCursor xCursor = xText.createTextCursor();
        xCursor.gotoEnd(false);
        xText.insertString(xCursor, sentence, true);
        xCursor.gotoEnd(false);
        //xText.insertControlCharacter(xCursor, ControlCharacter.PARAGRAPH_BREAK, false);


    }
}