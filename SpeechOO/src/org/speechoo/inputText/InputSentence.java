/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.inputText;

/**
 *
 * @author Hugo Santos
 */
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XSentenceCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import org.speechoo.SpeechOO;

public class InputSentence {

    PostProcessor postProcessor = new PostProcessor();

    public void insertNewSentence(String sentence) {
        //System.out.println("insertNewSentence");

        sentence = postProcessor.processUtterance(sentence);
        XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,
                SpeechOO.m_xFrame.getController().getModel());
        XText xText = xDoc.getText();
        XTextCursor xCursor = xText.createTextCursor();
        xCursor.gotoEnd(false);
        InputEditor.setBold(xCursor);
        InputEditor.setItalic(xCursor);
        InputEditor.setFontSize(xCursor, 22);
        InputEditor.setUnderline(xCursor);
//        InputEditor.setParaPosCenter(xCursor);
//        InputEditor.setParaPosLeft(xCursor);
//        InputEditor.setParaPosRight(xCursor);
        InputEditor.setParaPosBlock(xCursor);

        XSentenceCursor xSC = (XSentenceCursor) UnoRuntime.queryInterface(
                XSentenceCursor.class, xCursor);

        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xCursor);

        if (!xSC.gotoPreviousSentence(true)) {
            System.out.println("Não voltou sentença");
        }

        xText.insertString(xCursor, sentence, true);
//
//        if (!xPC.gotoEndOfParagraph(false)) {
//            System.out.println("Não foi pro final parágrafo");
//        }
        xCursor.gotoEnd(false);
        
        //xText.insertControlCharacter(xCursor, ControlCharacter.PARAGRAPH_BREAK, false);


    }
}
