/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.recognized;

import com.sun.star.awt.FontUnderline;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XController;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XParagraphCursor;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.text.XWordCursor;
import com.sun.star.uno.UnoRuntime;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.speechoo.SpeechOO;
import org.speechoo.inputText.InputEditor;
import org.speechoo.util.Numbers;


/**
 *
 * @author Welton Araújo
 */
public class CommandsListener extends ResultAdapter{
    
    @Override
    public void resultAccepted(ResultEvent e) {
        Numbers comp = new Numbers();
        XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,
                SpeechOO.m_xFrame.getController().getModel());
        XText xText = xDoc.getText();
        XController xController = xDoc.getCurrentController();
        XTextViewCursorSupplier xViewCursorSupplier = (XTextViewCursorSupplier)UnoRuntime.queryInterface(
        XTextViewCursorSupplier.class, xController);
        XTextViewCursor xCursor = xViewCursorSupplier.getViewCursor();
        XText xDocumentText = xCursor.getText();
        XTextCursor xModelCursor = xDocumentText.createTextCursorByRange(xCursor.getStart());
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        StringBuffer returnTokens = new StringBuffer();
        String Recognized, RecognizedAux = " ", numero;
        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xModelCursor);
        XWordCursor xWC = (XWordCursor) UnoRuntime.queryInterface(
                XWordCursor.class, xModelCursor);
        float number=0;
        Result r = (Result) (e.getSource());
        ResultToken tokens[] = r.getBestTokens();
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0)
                returnTokens.append(' ');
            returnTokens.append(tokens[i].getSpokenText());
        Recognized = returnTokens.toString();
        SpeechOO.label.setText(Recognized);
        SpeechOO.label.setVisible(true);
       }
        Recognized = returnTokens.toString();
        System.out.println("Comando: "+Recognized);
        if(Recognized.equals("voltaparagrafo")== true || Recognized.equals("marcarparagrafo") == true || Recognized.equals("avançaparagrafo") == true){
        RecognizedAux = Recognized.substring((Recognized.length()-9), Recognized.length());
        if(RecognizedAux.equals("paragrafo")==true){
            RecognizedAux = Recognized.substring(0,(Recognized.length()-9));
            if(RecognizedAux.equals("volta")==true){
            xPC.gotoPreviousParagraph(false);
            xCursor.gotoRange(xModelCursor.getStart(), false);
            }
            if(RecognizedAux.equals("avança")==true){
            xPC.gotoNextParagraph(false);
            xCursor.gotoRange(xModelCursor.getStart(), false);
            }
            if(RecognizedAux.equals("marcar")==true){
               if(xPC.isStartOfParagraph()){
               xPC.gotoEndOfParagraph(false);
               xCursor.gotoRange(xModelCursor.getStart(), true);
               }
               else {
                   xPC.gotoStartOfParagraph(false);
                   xCursor.gotoRange(xModelCursor.getStart(), false);
                   xPC.gotoEndOfParagraph(false);
                   xCursor.gotoRange(xModelCursor.getStart(), true);
               }
               }
        }
        }
        RecognizedAux = Recognized.substring(0, 5);

        if(RecognizedAux.equals("fonte")==true){
        numero = Recognized.substring(5);
        number = comp.compare(numero);
        InputEditor.setFontSize(xCursor, number);
        }
        if(Recognized.equals("voltar")==true){
            xWC.gotoPreviousWord(true);
            xCursor.gotoRange(xModelCursor.getStart(), false);
        }
        if(Recognized.equals("avançar")==true){
            xWC.gotoNextWord(false);
            xCursor.gotoRange(xModelCursor.getStart(), false);
        }
        if(Recognized.equals("marcar")==true){
            if(xWC.isStartOfWord()){
            xWC.gotoEndOfWord(false);
            xCursor.gotoRange(xModelCursor.getStart(), true);
            }
            else {
            xWC.gotoStartOfWord(false);
            xCursor.gotoRange(xModelCursor.getStart(), false);
            xWC.gotoEndOfWord(false);
            xCursor.gotoRange(xModelCursor.getStart(), true);
            }

        }
        if(Recognized.equals("doispontos")==true){
           xText.insertString(xCursor, ":", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("ponto")==true){
           xText.insertString(xCursor, ".", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("virgula")==true){
           xText.insertString(xCursor, ",", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("maior")==true){
           xText.insertString(xCursor, ">", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("menor")==true){
           xText.insertString(xCursor, "<", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("sublinhar")==true){
            try {
                if (new Short(FontUnderline.SINGLE).equals(xCursorProps.getPropertyValue("CharUnderline"))) {
                InputEditor.BackUnderline(xCursor);
                }
                else {
                    InputEditor.setUnderline(xCursor);
                }
            } catch (UnknownPropertyException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(Recognized.equals("centralizar")== true){
            InputEditor.setParaPosCenter(xCursor);
        }
        if(Recognized.equals("alinharparadireita")== true){
            InputEditor.setParaPosRight(xCursor);
        }
        if(Recognized.equals("alinharparaesquerda")== true){
            InputEditor.setParaPosLeft(xCursor);
        }
        if(Recognized.equals("justificar")== true){
            InputEditor.setParaPosBlock(xCursor);
        }
        if(Recognized.equals("negrito")==true){
            try {
                if (new Float(FontWeight.BOLD).equals(xCursorProps.getPropertyValue("CharWeight")) == true) {
                    InputEditor.BackBold(xCursor);
                } else {
                    InputEditor.setBold(xCursor);
                }
            } catch (UnknownPropertyException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(Recognized.equals("itálico")==true){
            try {
                if (com.sun.star.awt.FontSlant.ITALIC.equals(xCursorProps.getPropertyValue("CharPosture")) == true) {
                    InputEditor.BackItalic(xCursor);
                }
                else{
                    InputEditor.setItalic(xCursor);
                }
            } catch (UnknownPropertyException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
     }
 }

