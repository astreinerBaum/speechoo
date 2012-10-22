/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.speechoo.SpeechOO;

/**
 *
 * @author Hugo Santos
 */
public class SpeechPropertiesCreator {
    //insere o speech.properties na home do usuário caso ele não exista

    public static void create() {
        String userDir = System.getProperty("user.home");
        File dir = new File(userDir + "/speech.properties");

        if (!dir.exists()) {
            SpeechOO.logger = org.apache.log4j.Logger.getLogger(SpeechPropertiesCreator.class.getName());
            SpeechOO.logger.info("speech.properties inexistente");
            BufferedWriter speechProperties;
            try {
                speechProperties = new BufferedWriter(new FileWriter(userDir + "/speech.properties"));
                speechProperties.write("PSAPIRecognizerEngineCentral=br.ufpa.laps.jlapsapi.jsapi.JLaPSAPIEngineCentral");
                speechProperties.close();
            } catch (IOException ex) {
              SpeechOO.logger = org.apache.log4j.Logger.getLogger(SpeechPropertiesCreator.class.getName());
              SpeechOO.logger.error(ex);
            }
              SpeechOO.logger = org.apache.log4j.Logger.getLogger(SpeechPropertiesCreator.class.getName());
              SpeechOO.logger.info("speech.properties criado em: " + dir.getAbsolutePath());
        }
    }
}
