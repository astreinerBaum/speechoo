/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.recognized;

/*
 *
 * @author Hugo Santos
 */


import javax.speech.recognition.FinalDictationResult;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;
import org.speechoo.inputText.InputSentence;

public class FreeDictationListener extends ResultAdapter{

    InputSentence inputSentence = new InputSentence();
    public static ResultToken[][] tokensA  = null;
    
    @Override
    public void resultAccepted(ResultEvent e) {
        System.out.println("resultAccepted");
        StringBuffer returnTokens = new StringBuffer();
        //StringBuffer returnTokensA = new StringBuffer();
        FinalDictationResult r = (FinalDictationResult) (e.getSource());
        ResultToken tokens[] = r.getBestTokens();
        tokensA = r.getAlternativeTokens(null, null, 0);
       //r.getUnfinalizedTokens();
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0)
                returnTokens.append(' ');
            returnTokens.append(tokens[i].getSpokenText());
        }
        inputSentence.insertNewSentence(returnTokens.toString(), tokens.length);

         
      //  for (int i = 0; i < tokensA.length; i++) {
       //     System.out.println(i);
       //     for(int j=0; j<tokensA[i].length; j++)
      //      System.out.print(tokensA[i][j].getSpokenText()+ " ");
      //  }
         
     //   inputSentence.insertNewSentence(returnTokens.toString(), tokens.length);
        //teste...
        //inputSentence.insertNewSentence(returnTokensA.toString());
    }

    @Override
    public void resultRejected(ResultEvent e) {

    }
}