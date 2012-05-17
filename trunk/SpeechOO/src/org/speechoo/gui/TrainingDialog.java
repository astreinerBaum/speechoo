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
import com.sun.star.beans.XPropertySet;
import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.lang.EventObject;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.SpeechOO;
import org.speechoo.util.CaptureAudio;

/**
 *
 * @author Hugo Santos
 */
public class TrainingDialog {

    private String texts[];
    private static int train = 0;
    private XPropertySet xBackButtonPropertySet, xRecordButtonPropertySet,
            xStopButtonPropertySet, xNextButtonPropertySet;
    private XButton xbBack = null, xbRecord, xbStop, xbNext;
    private CaptureAudio ca = new CaptureAudio();
    private String recordsPath = System.getProperty("user.home") 
            + File.separator + "coruja_jlapsapi" + File.separator + "records" + File.separator;
    private File wav;

    public TrainingDialog() {
        XPackageInformationProvider xPackageInformationProvider =
                PackageInformationProvider.get(SpeechOO.m_xContext);
        String oxtLocation = xPackageInformationProvider.
                getPackageLocation(SpeechOO.extensionIdentifier).substring(7);//retira file:/

        File trainingTexts = new File(oxtLocation + "/dialogs/trainingtexts.xml");
        ReadFromXMLFile rfxf = new ReadFromXMLFile(trainingTexts);
        texts = rfxf.getTextsByLanguage("pt");
    }

    public void train() {
        try {
            final Dialog dialog1 = new Dialog(SpeechOO.m_xContext, 500, 500,
                    100, 250, "SpeechOO Records", "marralo");
            dialog1.insertTextField(10, 10, 230, 60, texts[0]);

//##############################################################################
            Object buttonPropertiesBack = dialog1.insertButton(50, 80, 30, "Voltar",
                    PushButtonType.STANDARD_value, false, "backButton");

            XControl xButtonControlBack = dialog1.getxDialogContainer().getControl("backButton");

//##############################################################################
            wav = new File(recordsPath + "train" + train + ".wav");
            Object buttonPropertiesRecord;
            if (wav.exists()) {
                buttonPropertiesRecord = dialog1.insertButton(90, 80, 30, "Gravar",
                        PushButtonType.STANDARD_value, false, "recordButton");
            } else {
                buttonPropertiesRecord = dialog1.insertButton(90, 80, 30, "Gravar",
                        PushButtonType.STANDARD_value, true, "recordButton");
            }
            XControl xButtonControlRecord = dialog1.getxDialogContainer().getControl("recordButton");

//##############################################################################
            Object buttonPropertiesStop = dialog1.insertButton(130, 80, 30, "Parar",
                    PushButtonType.STANDARD_value, false, "stopButton");

            XControl xButtonControlStop = dialog1.getxDialogContainer().getControl("stopButton");

//##############################################################################
            Object buttonPropertiesNext = dialog1.insertButton(170, 80, 30, "Próximo",
                    PushButtonType.STANDARD_value, true, "nextButton");

            XControl xButtonControlNext = dialog1.getxDialogContainer().getControl("nextButton");

//##############################################################################
            //Keep buttons properties to set durinf runtime
            xBackButtonPropertySet = (XPropertySet) UnoRuntime.
                    queryInterface(XPropertySet.class, buttonPropertiesBack);
            xRecordButtonPropertySet = (XPropertySet) UnoRuntime.
                    queryInterface(XPropertySet.class, buttonPropertiesRecord);
            xStopButtonPropertySet = (XPropertySet) UnoRuntime.
                    queryInterface(XPropertySet.class, buttonPropertiesStop);
            xNextButtonPropertySet = (XPropertySet) UnoRuntime.
                    queryInterface(XPropertySet.class, buttonPropertiesNext);

            //Put the button into the interface
            xbBack = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlBack);

            xbBack.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        wav = new File(recordsPath + "train" + (train - 1) + ".wav");
                        if (wav.exists()) {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Regravar");
                        } else {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Gravar");
                        }
                        if (train == 0) {
                            System.out.println("VOLTAR train= " + train);
                        } else if (train == 1) {
                            xBackButtonPropertySet.setPropertyValue("Enabled", false);
                            dialog1.insertTextField(10, 10, 230, 60, texts[--train]);
                        } else {
                            dialog1.insertTextField(10, 10, 230, 60, texts[--train]);
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

            //Put the button into the interface
            xbRecord = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlRecord);

            xbRecord.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {

                    try {
                        xBackButtonPropertySet.setPropertyValue("Enabled", false);
                        xRecordButtonPropertySet.setPropertyValue("Enabled", false);
                        xStopButtonPropertySet.setPropertyValue("Enabled", true);
                        xNextButtonPropertySet.setPropertyValue("Enabled", false);

                        wav = new File(recordsPath + "train" + (train + 1) + ".wav");
                        ca.startRecording(wav);

                    } catch (Exception ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject arg0) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            //Put the button into the interface
            xbStop = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlStop);

            xbStop.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {

                    try {
                        xRecordButtonPropertySet.setPropertyValue("Enabled", true);

                        if (train != 0) {
                            xBackButtonPropertySet.setPropertyValue("Enabled", true);
                        }
                        xRecordButtonPropertySet.setPropertyValue("Label", "Regravar");
                        xStopButtonPropertySet.setPropertyValue("Enabled", false);
                        xNextButtonPropertySet.setPropertyValue("Enabled", true);

                        ca.stopRecording();

                    } catch (Exception ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject arg0) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            //Create Button
            xbNext = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlNext);

            xbNext.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        wav = new File(recordsPath + "train" + (train + 1) + ".wav");
                        if (wav.exists()) {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Regravar");
                        } else {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Gravar");
                        }
                        if (train == 0) {
                            xBackButtonPropertySet.setPropertyValue("Enabled", true);
                            dialog1.insertTextField(10, 10, 230, 60, texts[++train]);
                        } else if (train == texts.length) {
                            System.out.println("PROXIMO train: " + train + " último treino");
                        } else {
                            dialog1.insertTextField(10, 10, 230, 60, texts[++train]);
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

            dialog1.execute();
            dialog1.dispose();

        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        }
    }
}
