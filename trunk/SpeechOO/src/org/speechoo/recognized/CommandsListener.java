/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.recognized;

import javax.speech.recognition.Result;
import javax.speech.recognition.ResultAdapter;
import javax.speech.recognition.ResultEvent;
import javax.speech.recognition.ResultToken;

/**
 *
 * @author 10080000701
 */
public class CommandsListener extends ResultAdapter{

    @Override
    public void resultAccepted(ResultEvent e) {
        StringBuffer returnTokens = new StringBuffer();
        String Recognized;
        Result r = (Result) (e.getSource());
        ResultToken tokens[] = r.getBestTokens();
        for (int i = 0; i < tokens.length; i++) {
            if (i > 0)
                returnTokens.append(' ');
            returnTokens.append(tokens[i].getSpokenText());
        }
        //resultado dos comandos
        Recognized = returnTokens.toString();
        System.out.println("Comando: "+Recognized);

    }
}
