/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.recognized;

/*
 *
 * @author Hugo Santos
 */


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import org.speechoo.SpeechOO;
import org.speechoo.inputText.InputSentence;

public class FreeDictationListener extends ResultAdapter{

    InputSentence inputSentence = new InputSentence();
    
    @Override

public void resultAccepted(ResultEvent e) {

        System.out.println("resultAccepted");
        StringBuffer returnTokens = new StringBuffer();
        
        //StringBuffer returnTokensA = new StringBuffer();
        Result r = (Result) (e.getSource());
        ResultToken tokens[] = r.getBestTokens();
        //ResultToken tokensA[] = r.getUnfinalizedTokens();
       //r.getUnfinalizedTokens();
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0)
                returnTokens.append(' ');
            returnTokens.append(tokens[i].getSpokenText());
        }
        System.out.printf("Tamanho%d:\n", +tokens.length);
        SpeechOO.sinal = tokens.length;
        if(SpeechOO.sinal<=2){
            SpeechOO.gram.getActivationMode();
            }
        else{
        try {
            SpeechOO.gram.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(FreeDictationListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        inputSentence.insertNewSentence(returnTokens.toString());
        }
        
            
            inputSentence.insertNewSentence(returnTokens.toString());
        
        
       
        

        /*
        for (int i = 0; i < tokensA.length; i++) {
            if (i > 0)
                returnTokens.append(' ');
            returnTokensA.append(tokensA[i].getSpokenText());
        }
         */
        //teste...
        //inputSentence.insertNewSentence(returnTokensA.toString());
        
    }

    @Override
    public void resultRejected(ResultEvent e) {

    }
}