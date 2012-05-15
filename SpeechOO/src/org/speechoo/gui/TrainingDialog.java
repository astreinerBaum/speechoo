/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.gui;

import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.PushButtonType;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XButton;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.beans.XPropertySet;
import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.lang.EventObject;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.speechoo.SpeechOO;

/**
 *
 * @author 10080000701
 */
public class TrainingDialog {

    private String texts[];
    public static int train = 0;
    private Object buttonPropertiesRecord, buttonPropertiesStop, buttonPropertiesNext, buttonPropertiesBack;
    XButton xbBack = null, xbRecord, xbStop, xbNext;
    private XControl xButtonControlBack, xButtonControlRecord, xButtonControlStop, xButtonControlNext;

    public TrainingDialog() {
        XPackageInformationProvider xPackageInformationProvider = PackageInformationProvider.get(SpeechOO.m_xContext);
        String oxtLocation = xPackageInformationProvider.getPackageLocation(SpeechOO.extensionIdentifier).substring(7);//retira file:/
        //System.out.println("oxtLocation: "+oxtLocation.substring(7));//retira file:/

        File trainingTexts = new File(oxtLocation + "/dialogs/trainingtexts.xml");
        ReadFromXMLFile rfxf = new ReadFromXMLFile(trainingTexts);
        texts = rfxf.getTextsByLanguage("pt");
        //this.texts = texts;

    }

    public void train() {
        try {
            final Dialog dialog1 = new Dialog(SpeechOO.m_xContext, 500, 500, 100, 250, "SpeechOO Records", "marralo");
            dialog1.insertTextField(10, 10, 230, 60, texts[0]);
            //System.out.println("textwidth:"+texts[0].length());
            //System.out.println("textFrame");
            //dialog.insertTextFrame(10, 10, 300, texts[0]);
            //dialog.insertTextField(

            //Object buttonPropertiesRecord, buttonPropertiesStop, buttonPropertiesNext, buttonPropertiesBack;

            //Keep button properties object to change when needed
            buttonPropertiesBack = dialog1.insertButton(50, 80, 30, "Voltar", PushButtonType.STANDARD_value, false, "backButton");

            xButtonControlBack = dialog1.getxDialogContainer().getControl("backButton");
            //Put the button into the interface
            xbBack = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlBack);

            xbBack.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        if (train == 0) {
                            System.out.println("VOLTAR train=0");
                        } else if (train == 1) {
                            XPropertySet xButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, buttonPropertiesBack);
                            xButtonPropertySet.setPropertyValue("Enabled", false);
                            dialog1.insertTextField(10, 10, 230, 60, texts[--train]);
                            System.out.println("VOLTAR train=1 train-- ; deve desativar botão voltar");
                        } else {
                            dialog1.insertTextField(10, 10, 230, 60, texts[--train]);
                            System.out.println("VOLTAR train--");
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject arg0) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            
            //Keep button properties object to change when needed
            buttonPropertiesRecord = dialog1.insertButton(90, 80, 30, "Gravar", PushButtonType.STANDARD_value, true, "recordButton");

            xButtonControlRecord = dialog1.getxDialogContainer().getControl("recordButton");

            //Put the button into the interface
            xbRecord = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlRecord);
            //xbgravar = dialog1.insertButton(90, 80, 30, "Gravar", PushButtonType.STANDARD_value, true);
            xbRecord.addActionListener(new XActionListener() {

            public void actionPerformed(ActionEvent arg0) {
            System.out.println("gravar");
            throw new UnsupportedOperationException("Not supported yet.");
            }

            public void disposing(EventObject arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
            }
            });

            //Keep button properties object to change when needed
            buttonPropertiesStop = dialog1.insertButton(130, 80, 30, "Parar", PushButtonType.STANDARD_value, true, "stopButton");

            xButtonControlStop = dialog1.getxDialogContainer().getControl("stopButton");

            //Put the button into the interface
            xbStop = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlStop);
            //xbparar = dialog1.insertButton(130, 80, 30, "Parar", PushButtonType.STANDARD_value, true);
            xbStop.addActionListener(new XActionListener() {

            public void actionPerformed(ActionEvent arg0) {
            System.out.println("parar");
            throw new UnsupportedOperationException("Not supported yet.");
            }

            public void disposing(EventObject arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
            }
            });

            //Keep button properties object to change when needed
            buttonPropertiesNext = dialog1.insertButton(170, 80, 30, "Próximo", PushButtonType.STANDARD_value, true, "nextButton");

            xButtonControlNext = dialog1.getxDialogContainer().getControl("nextButton");
            //Create Button
            xbNext = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlNext);

            xbNext.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        
                        if (train == 0){
                            XPropertySet xButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, buttonPropertiesBack);
                            xButtonPropertySet.setPropertyValue("Enabled", true);
                            dialog1.insertTextField(10, 10, 230, 60, texts[++train]);
                            System.out.println("PROXIMO train=0");
                        } else if (train == 4) {
                            System.out.println("PROXIMO train=4");
                        } else {
                            dialog1.insertTextField(10, 10, 230, 60, texts[++train]);
                            System.out.println("PROXIMO train++");
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject arg0) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
            //System.out.println("181");
            //dialog1.editButton();
            short returnValue = dialog1.execute();
            dialog1.dispose();
        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        }
    }
}
