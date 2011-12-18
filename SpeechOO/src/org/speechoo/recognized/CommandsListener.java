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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import org.speechoo.SpeechOO;
import org.speechoo.inputText.InputEditor;

/**
 *
 * @author 10080000701
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
        XTextViewCursor xCursor2 = xViewCursorSupplier.getViewCursor();
        XText xDocumentText = xCursor2.getText();
        XTextCursor xModelCursor = xDocumentText.createTextCursorByRange(xCursor2.getStart());
        XTextCursor xModelCursor2 = xDocumentText.createTextCursorByRange(xCursor2.getStart());
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor2);
        StringBuffer returnTokens = new StringBuffer();
        String Recognized, Recognized2 = " ", numero;
        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xModelCursor);
        XParagraphCursor xPC2= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xModelCursor2);
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
       }
        //resultado dos comandos
        
        Recognized = returnTokens.toString();
        System.out.println("Comando: "+Recognized);
        if(Recognized.equals("voltaparagrafo")== true || Recognized.equals("celecionarparagrafo") == true || Recognized.equals("avançaparagrafo") == true){
        Recognized2 = Recognized.substring((Recognized.length()-9), Recognized.length());
        if(Recognized2.equals("paragrafo")==true){
            Recognized2 = Recognized.substring(0,(Recognized.length()-9));
            if(Recognized2.equals("volta")==true){
            System.out.println("entrou!");
            xPC.gotoPreviousParagraph(false);
            xCursor2.gotoRange(xModelCursor.getStart(), false);
            }
            if(Recognized2.equals("avança")==true){
            System.out.println("entrou!2");
            xPC.gotoNextParagraph(false);
            xCursor2.gotoRange(xModelCursor.getStart(), false);
            }
            if(Recognized2.equals("marcar")==true){
               System.out.println("Entrou");
               if(xPC.isStartOfParagraph()){
               xPC.gotoEndOfParagraph(false);
               xCursor2.gotoRange(xModelCursor.getStart(), true);
               }
               else {
                   xPC.gotoStartOfParagraph(false);
                   xCursor2.gotoRange(xModelCursor.getStart(), false);
                   xPC.gotoEndOfParagraph(false);
                   xCursor2.gotoRange(xModelCursor.getStart(), true);
               }
               }
        }
        }
        Recognized2 = Recognized.substring(0, 5);

        if(Recognized2.equals("fonte")==true){
        numero = Recognized.substring(5);
        number = comp.compare(numero);
        InputEditor.setFontSize(xCursor2, number);
        }
        if(Recognized.equals("voltar")==true){
            xWC.gotoPreviousWord(true);
            xCursor2.gotoRange(xModelCursor.getStart(), false);
        }
        if(Recognized.equals("avançar")==true){
            xWC.gotoNextWord(true);
            xCursor2.gotoRange(xModelCursor.getStart(), false);
        }
        if(Recognized.equals("marcar")==true){
            if(xWC.isStartOfWord()){
            xWC.gotoEndOfWord(false);
            xCursor2.gotoRange(xModelCursor.getStart(), true);
            }
            else {
            xWC.gotoStartOfWord(false);
            xCursor2.gotoRange(xModelCursor.getStart(), false);
            xWC.gotoEndOfWord(false);
            xCursor2.gotoRange(xModelCursor.getStart(), true);
            }

        }
        if(Recognized.equals("doispontos")==true){
           xText.insertString(xCursor2, ":", true);
           xCursor2.gotoRange(xCursor2.getEnd(), false);
        }
        if(Recognized.equals("ponto")==true){
           xText.insertString(xCursor2, ".", true);
           xCursor2.gotoRange(xCursor2.getEnd(), false);
        }
        if(Recognized.equals("virgula")==true){
           xText.insertString(xCursor2, ",", true);
           xCursor2.gotoRange(xCursor2.getEnd(), false);
        }
        if(Recognized.equals("maior")==true){
           xText.insertString(xCursor2, ">", true);
           xCursor2.gotoRange(xCursor2.getEnd(), false);
        }
        if(Recognized.equals("menor")==true){
           xText.insertString(xCursor2, "<", true);
           xCursor2.gotoRange(xCursor2.getEnd(), false);
        }
        if(Recognized.equals("sublinhar")==true){
            try {
                if (new Short(FontUnderline.SINGLE).equals(xCursorProps.getPropertyValue("CharUnderline"))) {
                System.out.println("ENTROU!");
                    InputEditor.BackUnderline(xCursor2);
                }
                else {
                    InputEditor.setUnderline(xCursor2);
                }
            } catch (UnknownPropertyException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if(Recognized.equals("negrito")==true){
            try {
                if (new Float(FontWeight.BOLD).equals(xCursorProps.getPropertyValue("CharWeight")) == true) {
                    InputEditor.BackBold(xCursor2);
                } else {
                    InputEditor.setBold(xCursor2);
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
                    InputEditor.BackItalic(xCursor2);
                }
                else{
                    InputEditor.setItalic(xCursor2);
                }
            } catch (UnknownPropertyException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WrappedTargetException ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
     }
 }

