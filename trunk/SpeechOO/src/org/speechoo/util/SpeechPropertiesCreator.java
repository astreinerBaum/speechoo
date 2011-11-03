/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author 10080000701
 */
public class SpeechPropertiesCreator {
    //insere o speech.properties na home do usuário caso ele não exista

    public static void create() {
        String userDir = System.getProperty("user.home");
        File dir = new File(userDir + "/speech.properties");

        if (!dir.exists()) {
            System.out.println("speech.properties inexistente");
            BufferedWriter speechProperties;
            try {
                speechProperties = new BufferedWriter(new FileWriter(userDir + "/speech.properties"));
                speechProperties.write("PSAPIRecognizerEngineCentral=br.ufpa.laps.jlapsapi.jsapi.JLaPSAPIEngineCentral");
                speechProperties.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            System.out.println("speech.properties criado em: " + dir.getAbsolutePath());
        }
    }
}
