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
import com.sun.star.awt.XFixedText;
import com.sun.star.awt.XTextComponent;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.SpeechOO;
import org.speechoo.util.Capture;
import ufpa.asr.frontend.Adaptador;
import ufpa.asr.frontend.AdaptationProgress;

/**
 *
 * @author Hugo Santos
 */
public class TrainingDialog extends Thread {

    private String texts[];
    private static int train = -1;
    private XPropertySet xBackButtonPropertySet, xRecordButtonPropertySet,
            xStopButtonPropertySet, xNextButtonPropertySet;
    private XButton xbBack = null, xbRecord, xbStop, xbNext;
    private XFixedText xFixedText;
    private XTextComponent xTextComponent;
    private Capture capture = new Capture();
    private String recordsPath = System.getProperty("user.home")
            + File.separator + "coruja_jlapsapi" + File.separator + "records" + File.separator;
    private File wav;
    private Dialog speakerAdaptationWindow;
    private static AdaptationProgress adaptationProgress;
    protected boolean closeWindow = false;

    public TrainingDialog() {
        XPackageInformationProvider xPackageInformationProvider =
                PackageInformationProvider.get(SpeechOO.m_xContext);
        String oxtLocation = xPackageInformationProvider.
                getPackageLocation(SpeechOO.extensionIdentifier).substring(7);//retira file:/

        File trainingTexts = new File(oxtLocation + "/dialogs/trainingtexts.xml");
        ReadFromXMLFile rfxf = new ReadFromXMLFile(trainingTexts);
        texts = rfxf.getTextsByLanguage("pt");
        try {
            speakerAdaptationWindow = new Dialog(SpeechOO.m_xContext, 500, 500,
                    180, 250, "SpeechOO Records", "marralo");
            speakerAdaptationWindow.insertFixedText(10, 10, 280,
                    "1 - Cheque o volume do microfone para que as gravações não fiquem"
                    + " muito baixas.\n"
                    + "2 - Evite capturar ruídos durante a gravação.\n"
                    + "3 - Ao concluir a gravação, o processo demorará até 10 minutos.");
        } catch (Exception ex) {
            Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void train() {
        try {
            train = -1;
//######### Keep buttons properties to set during runtime ######################
            Object buttonPropertiesBack = speakerAdaptationWindow.insertButton(50, 160, 30, "Voltar",
                    PushButtonType.STANDARD_value, false, "backButton");

            XControl xButtonControlBack = speakerAdaptationWindow.getxDialogContainer().getControl("backButton");

//########## Keep buttons properties to set during runtime #####################
            Object buttonPropertiesRecord = buttonPropertiesRecord = speakerAdaptationWindow.insertButton(90, 160, 30, "Gravar",
                    PushButtonType.STANDARD_value, false, "recordButton");
            XControl xButtonControlRecord = speakerAdaptationWindow.getxDialogContainer().getControl("recordButton");

//########## Keep buttons properties to set during runtime #####################
            Object buttonPropertiesStop = speakerAdaptationWindow.insertButton(130, 160, 30, "Parar",
                    PushButtonType.STANDARD_value, false, "stopButton");

            XControl xButtonControlStop = speakerAdaptationWindow.getxDialogContainer().getControl("stopButton");

//########## Keep buttons properties to set during runtime #####################
            Object buttonPropertiesNext = speakerAdaptationWindow.insertButton(170, 160, 30, "Próximo",
                    PushButtonType.STANDARD_value, true, "nextButton");

            XControl xButtonControlNext = speakerAdaptationWindow.getxDialogContainer().getControl("nextButton");

//########## Keep buttons properties to set during runtime #####################
            xBackButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, buttonPropertiesBack);
            xRecordButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, buttonPropertiesRecord);
            xStopButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, buttonPropertiesStop);
            xNextButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, buttonPropertiesNext);

//########## Information of the adapting status ################################

            adaptationProgress = new AdaptationProgress() {

                int numberOfFiles;
                int currentFile = 0;

                public void setNumberOfFiles(int numberOfFiles) {
                    this.numberOfFiles = numberOfFiles;
                    System.out.println("Número de arquivos: " + this.numberOfFiles);
                }

                public void currentExtractFile() {
                    System.out.println("Analisando arquivo: " + (++currentFile) + "/" + this.numberOfFiles);
                    xTextComponent.setText("Analisando arquivo:" + currentFile + "/" + numberOfFiles);
                }

                public void adapting() {
                    System.out.println("Adaptando - aguarde");
                    xTextComponent.setText("Adaptando - aguarde");
                }

                public void finished() {
                    try {
                        System.out.println("Adaptação concluída");
                        xTextComponent.setText("Adaptação concluída");
                        xNextButtonPropertySet.setPropertyValue("PushButtonType", (short) PushButtonType.OK_value);
                        xNextButtonPropertySet.setPropertyValue("Enabled", true);
                        closeWindow = true;
                    } catch (UnknownPropertyException ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (WrappedTargetException ex) {
                        Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            xFixedText = speakerAdaptationWindow.insertFixedText(10, 45, 230, "");
            xTextComponent = speakerAdaptationWindow.insertTextField(10, 60, 230, 90, "");

//########## Control of the buttons ############################################

            //Put the button into the interface
            xbBack = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlBack);

            xbBack.addActionListener(new XActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    try {
                        wav = new File(recordsPath + "train" + train + ".wav");
                        if (wav.exists()) {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Regravar");
                        } else {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Gravar");
                        }
                        if (train == 0) {
                            xBackButtonPropertySet.setPropertyValue("Enabled", false);
                            xRecordButtonPropertySet.setPropertyValue("Enabled", false);
                            xFixedText.setText("");
                            xTextComponent.setText("");
                            train--;
                        } else if (train == 1) {
                            xFixedText.setText("Treino " + (--train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        } else if (train == (texts.length - 1)) {
                            xRecordButtonPropertySet.setPropertyValue("Enabled", true);
                            xNextButtonPropertySet.setPropertyValue("PushButtonType", (short) PushButtonType.STANDARD_value);
                            xNextButtonPropertySet.setPropertyValue("Label", "Próximo");
                            xNextButtonPropertySet.setPropertyValue("Enabled", true);
                            xFixedText.setText("Treino " + (--train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        } else {
                            xFixedText.setText("Treino " + (--train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
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
                        System.out.println("gravando: " + recordsPath + "train" + (train + 1) + ".wav");
                        wav = new File(recordsPath + "train" + (train + 1) + ".wav");
                        //ca.startRecording(wav);
                        capture.start(wav);

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
                        xBackButtonPropertySet.setPropertyValue("Enabled", true);

                        //ca.stopRecording();
                        capture.stop();

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
                        xRecordButtonPropertySet.setPropertyValue("Enabled", true);
                        wav = new File(recordsPath + "train" + (train + 2) + ".wav");
                        if (wav.exists()) {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Regravar");
                        } else {
                            xRecordButtonPropertySet.setPropertyValue("Label", "Gravar");
                        }
                        if (train == -1) {
                            xBackButtonPropertySet.setPropertyValue("Enabled", true);
                            xFixedText.setText("Treino " + (++train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        } else if (train == (texts.length - 2)) {
                            xFixedText.setText("Treino " + (++train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        } else if (train == (texts.length - 1)) {
                            xRecordButtonPropertySet.setPropertyValue("Enabled", false);
                            xNextButtonPropertySet.setPropertyValue("Label", "Adaptar");
                            xNextButtonPropertySet.setPropertyValue("PushButtonType", (short) PushButtonType.OK_value);
                            xFixedText.setText("Adaptação");
                            xTextComponent.setText("");
                            train++;
                        } else if (train == texts.length) {
                            if (!closeWindow) {
                                Thread thread = new TrainingDialog();
                                thread.start();
                                xRecordButtonPropertySet.setPropertyValue("Enabled", false);
                                xNextButtonPropertySet.setPropertyValue("Enabled", false);
                                xNextButtonPropertySet.setPropertyValue("Label", "Concluir");                              
                            } else {
                                System.out.println("Deve fechar");
                            }
                        } else if (train == -1) {
                            xFixedText.setText("Treino " + (++train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        } else {
                            xFixedText.setText("Treino " + (++train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
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

            speakerAdaptationWindow.execute();
            speakerAdaptationWindow.dispose();

        } catch (com.sun.star.uno.Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        Adaptador adapter = new Adaptador(adaptationProgress);
        adapter.startAdaptation();
    }
}
