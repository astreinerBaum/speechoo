/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.util;

import com.sun.star.awt.XPrinterServer;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.uno.UnoRuntime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.SpeechOO;
import org.speechoo.inputText.InputSentence;

/**
 *
 * @author 10080000501
 */

public class PrintAndSave {
public static int flag=0;

public static void saveDocument(String name) {
// Save the document
String storeUrl = "File:"+System.getProperty("user.home")+"/Documents/"+name+".odt";
String stringLength = "Nome do Arquivo: "+name+ "  "+"Salvo em: "+storeUrl;
SpeechOO.label.setText("Nome do Arquivo: "+name+ "  "+"Salvo em: "+storeUrl);
SpeechOO.frame.setSize((5*stringLength.length()+200), 50);
SpeechOO.frame.setVisible(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(PrintAndSave.class.getName()).log(Level.SEVERE, null, ex);
        }
XStorable xStorable = ( XStorable )UnoRuntime.queryInterface(XStorable.class, InputSentence.xDoc);
PropertyValue[] storeProps = new PropertyValue[0];
        try {
            xStorable.storeAsURL(storeUrl, storeProps);
        } catch (IOException ex) {
            Logger.getLogger(PrintAndSave.class.getName()).log(Level.SEVERE, null, ex);
        }
SpeechOO.frame.setLocation(850, 1000);
SpeechOO.frame.setSize(200, 50);
SpeechOO.frame.setVisible(true);
}
public static void printDocument(){

XPrinterServer xPrintable = (XPrinterServer)UnoRuntime.queryInterface(XPrinterServer.class, InputSentence.xDoc);
//XPrintJob  xPrintJob  = (XPrintJob)UnoRuntime.queryInterface(XPrintJob.class, InputSentence.xDoc);
//System.out.println(xPrintJob.getPrinter().length);
String[] printerDesc = new String[8];


printerDesc = xPrintable.getPrinterNames();
System.out.println(printerDesc[0]);
System.out.println(printerDesc[1]);
/*System.out.println("| : "+printerDesc[0].Name+ " : |");
System.out.println("| : "+printerDesc[0].Value+ " : |");
System.out.println("| : "+printerDesc[1].Name+ " : |");
System.out.println("| : "+printerDesc[1].Value+ " : |");
System.out.println("| : "+printerDesc[2].Name+ " : |");
System.out.println("| : "+printerDesc[2].Value+ " : |");
System.out.println("| : "+printerDesc[3].Name+ " : |");
System.out.println("| : "+printerDesc[3].Value+ " : |");
System.out.println("| : "+printerDesc[4].Name+ " : |");
System.out.println("| : "+printerDesc[4].Value+ " : |");
System.out.println("| : "+printerDesc[5].Name+ " : |");
System.out.println("| : "+printerDesc[5].Value+ " : |");
System.out.println("| : "+printerDesc[6].Name+ " : |");
System.out.println("| : "+printerDesc[6].Value+ " : |");
System.out.println("| : "+printerDesc[7].Name+ " : |");
System.out.println("| : "+printerDesc[7].Value+ " : |");
printerDesc2 = xPrintable.getPrinter();
System.out.println("| : "+printerDesc2[0].Name+ " : |");
System.out.println("| : "+printerDesc2[0].Value+ " : |");
System.out.println("| : "+printerDesc2[1].Name+ " : |");
System.out.println("| : "+printerDesc2[1].Value+ " : |");
System.out.println("| : "+printerDesc2[2].Name+ " : |");
System.out.println("| : "+printerDesc2[2].Value+ " : |");
System.out.println("| : "+printerDesc2[3].Name+ " : |");
System.out.println("| : "+printerDesc2[3].Value+ " : |");
System.out.println("| : "+printerDesc2[4].Name+ " : |");
System.out.println("| : "+printerDesc2[4].Value+ " : |");
System.out.println("| : "+printerDesc2[5].Name+ " : |");
System.out.println("| : "+printerDesc2[5].Value+ " : |");
System.out.println("| : "+printerDesc2[6].Name+ " : |");
System.out.println("| : "+printerDesc2[6].Value+ " : |");
System.out.println("| : "+printerDesc2[7].Name+ " : |");
System.out.println("| : "+printerDesc2[7].Value+ " : |");
try {
            xPrintable.setPrinter(printerDesc);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(PrintAndSave.class.getName()).log(Level.SEVERE, null, ex);
        }
System.out.println(xPrintable.getPrinter().length);*/
//if(xPrintable.getPrinter().length > 1){
 //   System.out.println("existe mais de uma impressora");
//}

//printerDesc = xPrintable.getPrinter();
PropertyValue[] printOpts = new PropertyValue[1];
printOpts[0] = new PropertyValue();
printOpts[0].Name = "Pages";
printOpts[0].Value = "1";
//xPrintable.print(printOpts);
}
}
