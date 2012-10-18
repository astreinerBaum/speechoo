package org.speechoo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Hugo Santos
 */

public class AcousticModelSelector {

    public static void main(String[] args) {
        String modelName = "Hugo";
        changeText("-h adaptacao/" + modelName + "/" + modelName + ".am");
    }

    public static void changeText(String newText) {
        String textReplaced = "";
        String userHome = System.getProperty("user.home");
        File f = new File(userHome + File.separator + "coruja_jlapsapi" + File.separator + "julius.jconf");      

        FileInputStream fs;
        InputStreamReader in;
        BufferedReader br;

        StringBuffer sb = new StringBuffer();

        String textinLine;

        try {
            fs = new FileInputStream(f);
            in = new InputStreamReader(fs);
            br = new BufferedReader(in);

            while (true) {
                textinLine = br.readLine();
                if (textinLine == null) {
                    break;
                }
                if (textinLine.startsWith("-h ")) {
                    textReplaced = textinLine;
                }
                sb.append(textinLine).append("\n");
            }
            String textToEdit1 = textReplaced;
            int cnt1 = sb.indexOf(textToEdit1);
            sb.replace(cnt1, cnt1 + textToEdit1.length(), newText);

            fs.close();
            in.close();
            br.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            FileWriter fstream = new FileWriter(f);
            BufferedWriter outobj = new BufferedWriter(fstream);
            outobj.write(sb.toString());
            outobj.close();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
