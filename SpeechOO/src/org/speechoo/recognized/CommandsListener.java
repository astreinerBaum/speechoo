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
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import org.speechoo.SpeechOO;
import org.speechoo.gui.InputDevicesControl;
import org.speechoo.inputText.InputEditor;
import org.speechoo.inputText.InputSentence;
import org.speechoo.util.CoGrOO;
import org.speechoo.util.Numbers;
import org.speechoo.util.PrintAndSave;
import org.speechoo.util.TableGramatical;
import org.speechoo.util.TableNames;

/**
 *
 * @author Welton Araújo
 */
public class CommandsListener extends ResultAdapter{
    
    @Override
    public void resultAccepted(ResultEvent e) {
        TableNames a = new TableNames();
        TableGramatical d = new TableGramatical();
        String AuxResults;
        Numbers comp = new Numbers();
        int j=0;
        int number = 0;
        String Recognized, RecognizedAux = " ", numero;
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
        XParagraphCursor xPC= (XParagraphCursor) UnoRuntime.queryInterface(
                XParagraphCursor.class, xModelCursor);

        XWordCursor xWC = (XWordCursor) UnoRuntime.queryInterface(
                XWordCursor.class, xModelCursor);
        Result r = (Result) (e.getSource());
        ResultToken tokens[] = r.getBestTokens();
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0)
                returnTokens.append(' ');
            returnTokens.append(tokens[i].getSpokenText());
        Recognized = returnTokens.toString();
        Recognized = Recognized.substring(0, 1).toUpperCase().concat(Recognized.substring(1));
        SpeechOO.label.setText(Recognized);
        SpeechOO.label.setVisible(true);
        Recognized = Recognized.toLowerCase();
        }

        Recognized = returnTokens.toString();
        System.out.println("Comando: "+Recognized);
        if(CoGrOO.gramaticalFlag == 1){
    CoGrOO.cellNumber = comp.compare(Recognized);
    System.out.println(TableGramatical.TableGramatical.getRowCount()-1);
    if(CoGrOO.cellNumber != 0){
            if(CoGrOO.cellNumber==-1 || CoGrOO.cellNumber > TableGramatical.TableGramatical.getRowCount()-1){
                //System.out.println("entrou");
                SpeechOO.label.setText("Fale Novamente");
                return;
            }
            String Right = TableGramatical.TableGramatical.getValueAt(CoGrOO.cellNumber, 1).toString();
            for(int i=0; i<CoGrOO.length-(CoGrOO.wrong.split(" ").length-1); i++){
                 while(j < CoGrOO.wrong.split(" ").length){
                 xWC.gotoPreviousWord(true);
                 j++;
                 }
                 xCursor.gotoRange(xModelCursor.getStart(), true);
                System.out.println(CoGrOO.wrong);
                System.out.println(xCursor.getString());
                 if(CoGrOO.wrong.equals(xCursor.getString())==true){
                System.out.println(CoGrOO.wrong);
                System.out.println(xCursor.getString());
                System.out.println(Right);
                xText.insertString(xCursor, Right, true);
            }
            xWC.gotoEndOfWord(true);
            xCursor.gotoRange(xModelCursor.getStart(), false);
            j=0;
        }
}
    CoGrOO.gramaticalFlag=0;
    TableGramatical.FrameGramatical.setEnabled(false);
    TableGramatical.FrameGramatical.dispose();
    xCursor.gotoEnd(false);
    SpeechOO.dic.setEnabled(true);
    SpeechOO.gram.setEnabled(false);
    SpeechOO.label.setText("Modo Ditado Ativado");
}
if(TableNames.FrameNames.isVisible()==true){
        number = comp.compare(Recognized);
          if(number!= 1 && number!= 2 && number!=3 && number!=4 && number!=5){
            SpeechOO.label.setText("Fale Novamente");
            return;
          }
        Recognized = TableNames.TableNames.getValueAt(number-1, 1).toString();
        InputEditor.changeFontName(xCursor, Recognized);
        TableNames.FrameNames.setEnabled(false);
        TableNames.FrameNames.dispose();
        SpeechOO.dic.setEnabled(true);
        SpeechOO.gram.setEnabled(false);
        SpeechOO.label.setText("Modo Ditado Ativado");
}
if(TableGramatical.FrameGramatical.isVisible()==true){
        number = comp.compare(Recognized);
          if(number!= 1 && number!= 2 && number!=3 && number!=4 && number!=5){
            SpeechOO.label.setText("Fale Novamente");
            return;
          }
        Recognized = "";
        Recognized = TableGramatical.TableGramatical.getValueAt(number-1, 1).toString();
        xCursor.gotoRange(InputSentence.xRange, true);
        xText.insertString(xCursor, Recognized, true);
        xCursor.gotoEnd(false);
        TableGramatical.FrameGramatical.setEnabled(false);
        TableGramatical.FrameGramatical.dispose();
        SpeechOO.dic.setEnabled(true);
        SpeechOO.gram.setEnabled(false);
        SpeechOO.label.setText("Modo Ditado Ativado");
}
        if(RecognizedAux.equals("voltar parágrafo")==true){
        xPC.gotoPreviousParagraph(false);
        xCursor.gotoRange(xModelCursor.getStart(), false);
        }
        if(RecognizedAux.equals("avançar parágrafo")==true){
        xPC.gotoNextParagraph(false);
        xCursor.gotoRange(xModelCursor.getStart(), false);
        }
        if(RecognizedAux.equals("selecionar parágrafo")==true){
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
        

if(Recognized.length()<6){
}
       else {
           RecognizedAux = Recognized.substring(0, 5);
       }
        if(RecognizedAux.equals("fonte")==true){
        numero = Recognized.substring(6);
        number = comp.compare(numero);
        if(number!=0){
        InputEditor.setFontSize(xCursor, number);
        }
        }
        if(Recognized.equals("voltar palavra")==true){
            xWC.gotoPreviousWord(true);
            xCursor.gotoRange(xModelCursor.getStart(), false);
        }
        if(Recognized.equals("avançar palavra")==true){
            xWC.gotoNextWord(false);
            xCursor.gotoRange(xModelCursor.getStart(), false);
        }
        if(Recognized.equals("selecionar palavra")==true){
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
        if(Recognized.equals("dois pontos")==true){
           xText.insertString(xCursor, ":", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("ponto")==true){
           xText.insertString(xCursor, ".", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("espaço")==true){
           xText.insertString(xCursor, " ", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("backspace")==true){
           xCursor.goLeft((short) 1, true);
           xText.insertString(xCursor, "", true);
        }
        if(Recognized.equals("ponto e vírgula")==true){
           xText.insertString(xCursor, ";", true);
           xCursor.gotoRange(xCursor.getEnd(), false);
        }
        if(Recognized.equals("mudar fonte")==true){
           a.Names();
        }
        if(Recognized.equals("corrigir")==true){
       TableGramatical.modelGramatical.setRowCount(5);
       TableGramatical.modelGramatical.setColumnCount(2);
       TableGramatical.TableGramatical.setValueAt(1, 0, 0);
       TableGramatical.TableGramatical.setValueAt(FreeDictationListener.tokensA[0][0], 0, 1);
        for(int aux = 1; aux<5; aux++){
           TableGramatical.TableGramatical.setValueAt(aux+1, aux, 0);
        }
        for(int aux = 0; aux<5; aux++){
           AuxResults = "";
           for(int aux2=0; aux2<FreeDictationListener.tokensA[aux].length; aux2++){
           AuxResults = AuxResults.concat(FreeDictationListener.tokensA[aux][aux2].getSpokenText()+ " ");
           }
           TableGramatical.TableGramatical.setValueAt(AuxResults, aux, 1);
        }
        d.Gramatical();
        }
        if(Recognized.equals("vírgula")==true){
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
        if(Recognized.equals("alinhar para direita")== true){
            InputEditor.setParaPosRight(xCursor);
        }
        if(Recognized.equals("alinhar para esquerda")== true){
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
    if(Recognized.equals("cor azul")){
            InputEditor.changeFontColor(xCursor, 0x0000FF);
    }
    if(Recognized.equals("cor marrom")){
            InputEditor.changeFontColor(xCursor, 0x4C1900);
    }
    if(Recognized.equals("cor preto")){
            InputEditor.changeFontColor(xCursor, 0x000000);
    }
    if(Recognized.equals("cor vermelho")){
            InputEditor.changeFontColor(xCursor, 0xFF0000);
    }
    if(Recognized.equals("cor verde")){
            InputEditor.changeFontColor(xCursor, 0x00AE00);
    }
    if(Recognized.equals("cor amarelo")){
            InputEditor.changeFontColor(xCursor, 0xFFFF00);
    }
    
if(Recognized.equals("salvar")){
SpeechOO.frame.setLocationRelativeTo((Component) SpeechOO.frame2);
SpeechOO.label.setText("Diga o nome do arquivo desejado");
SpeechOO.frame.setSize((5*"Diga o nome do arquivo desejado".length()+200), 50);
SpeechOO.frame.setVisible(true);
SpeechOO.dic.setEnabled(true);
SpeechOO.gram.setEnabled(false);
PrintAndSave.flag = 1;
}
if(Recognized.equals("enter")){
            try {
                InputDevicesControl.keyEnter();

            } catch (Exception ex) {
                Logger.getLogger(CommandsListener.class.getName()).log(Level.SEVERE, null, ex);
            }
}

    }
}




