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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.SpeechOO;
import org.speechoo.util.Capture;
import org.speechoo.util.ReadFromXMLFile;
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
    private XButton xbBack, xbRecord, xbStop, xbNext;
    private XFixedText xFixedText;
    private XTextComponent xTextComponent;
    private Capture capture = new Capture();
    private String userRecord = "";
    private String recordsPath = "";
    private String adaptacaoUserPath = "";
    private String adaptacaoPath = "";
    private File wav;
    private Dialog speakerAdaptationWindow;
    private static AdaptationProgress adaptationProgress;
    protected boolean closeWindow = false;

    public TrainingDialog() {
        XPackageInformationProvider xPackageInformationProvider =
                PackageInformationProvider.get(SpeechOO.m_xContext);
        String oxtLocation = xPackageInformationProvider.getPackageLocation(SpeechOO.extensionIdentifier).substring(7);//retira file:/

        File trainingTexts = new File(oxtLocation + "/dialogs/trainingtexts.xml");
        ReadFromXMLFile rfxf = new ReadFromXMLFile(trainingTexts);
        texts = rfxf.getTextsByLanguage("pt");
        try {
            speakerAdaptationWindow = new Dialog(SpeechOO.m_xContext, 500, 500,
                    180, 250, "SpeechOO Records", "");
            speakerAdaptationWindow.insertFixedText(10, 10, 48, 280,
                    "1 - Cheque o volume do microfone para que as gravações não fiquem"
                    + " muito baixas.\n"
                    + "2 - Evite capturar ruídos durante a gravação.\n"
                    + "3 - Ao concluir a gravação, o processo demorará até 10 minutos.");
        } catch (Exception ex) {
            Logger.getLogger(TrainingDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void train(final String userRecord) {
        this.userRecord = userRecord;
        String aux = System.getProperty("user.home") + File.separator
                + "coruja_jlapsapi" + File.separator + "adaptacao"
                + File.separator;

        recordsPath = aux + this.userRecord + File.separator + "records" + File.separator;
        adaptacaoUserPath = aux + this.userRecord + File.separator;
        adaptacaoPath = aux;

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
                    SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                    SpeechOO.logger.info("Número de arquivos: " + this.numberOfFiles);

                }

                public void currentExtractFile() {
                    SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                    SpeechOO.logger.info("Analisando arquivo: " + (++currentFile) + "/" + this.numberOfFiles);
                    xTextComponent.setText("Analisando arquivo:" + currentFile + "/" + numberOfFiles);
                }

                public void adapting() {
                    SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                    SpeechOO.logger.info("Adaptando - aguarde");
                    xTextComponent.setText("Adaptando - aguarde");

                }

                public void finished() {
                    try {
                        SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                        SpeechOO.logger.info("Adaptação concluída");
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

            xFixedText = speakerAdaptationWindow.insertFixedText(10, 45, 12, 230, "");
            xTextComponent = speakerAdaptationWindow.insertTextField(10, 60, 230, 90, "", true, true);

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
                        } else if (train == (texts.length)) {
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
                        SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                        SpeechOO.logger.error(ex);
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
                        SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                        SpeechOO.logger.info("gravando: " + recordsPath + "train" + (train + 1) + ".wav");
                        wav = new File(recordsPath + "train" + (train + 1) + ".wav");
                        wav.delete();
                        capture.start(wav);
                    } catch (Exception ex) {
                        SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                        SpeechOO.logger.error(ex);
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

                        capture.stop();

                    } catch (Exception ex) {
                        SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                        SpeechOO.logger.error(ex);
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
                            try {
                                File eraser = new File(adaptacaoUserPath + "wav_mfc_adapt.list");
                                eraser.delete();
                                eraser = new File(adaptacaoUserPath + "lab_adapt.list");
                                eraser.delete();
                                BufferedWriter wavMfcAdaptList, labAdaptList;
                                wavMfcAdaptList = new BufferedWriter(new FileWriter(adaptacaoUserPath + "wav_mfc_adapt.list"));
                                labAdaptList = new BufferedWriter(new FileWriter(adaptacaoUserPath + "lab_adapt.list"));
                                SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                                SpeechOO.logger.info("Escrevendo lista de arquivos gravados");
                                for (int i = 1; i <= texts.length; i++) {
                                    wav = new File(recordsPath + "train" + i + ".wav");
                                    if (wav.exists()) {

                                        labAdaptList.write(adaptacaoPath + "labs" + File.separator + "train" + i + ".lab");
                                        wavMfcAdaptList.write(recordsPath + "train" + i + ".wav");
                                        if (i != (texts.length)) {
                                            labAdaptList.newLine();
                                            wavMfcAdaptList.newLine();
                                        }
                                    }
                                }
                                labAdaptList.close();
                                wavMfcAdaptList.close();
                            } catch (IOException ex) {
                                SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                                SpeechOO.logger.error(ex);
                            }
                            xRecordButtonPropertySet.setPropertyValue("Enabled", false);
                            xNextButtonPropertySet.setPropertyValue("Label", "Adaptar");
                            xNextButtonPropertySet.setPropertyValue("PushButtonType", (short) PushButtonType.OK_value);
                            xFixedText.setText("Adaptação");
                            xTextComponent.setText("");
                            train++;
                        } else if (train == texts.length) {
                            if (!closeWindow) {
                                Thread thread = new TrainingDialog();
                                //nome da thread setado porque o valor do userRecord não chega no run() da thread
                                //uso o nome da thread para saber que é o userRecord, é gambiarra
                                thread.setName(userRecord);
                                thread.setPriority(10);
                                thread.start();
                                xRecordButtonPropertySet.setPropertyValue("Enabled", false);
                                xNextButtonPropertySet.setPropertyValue("Enabled", false);
                                xNextButtonPropertySet.setPropertyValue("Label", "Concluir");
                            } else {
                                speakerAdaptationWindow.close();
                            }
                        } else if (train == -1) {
                            xFixedText.setText("Treino " + (++train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        } else {
                            xFixedText.setText("Treino " + (++train + 1) + "/" + texts.length);
                            xTextComponent.setText(texts[train]);
                        }

                    } catch (Exception ex) {
                        SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                        SpeechOO.logger.error(ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject arg0) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            speakerAdaptationWindow.execute();
            speakerAdaptationWindow.dispose();

        } catch (Exception ex) {
            SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
            SpeechOO.logger.error(ex);
        }
    }

    public int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return count;
        } finally {
            is.close();
        }
    }

    @Override
    public void run() {
        Adaptador adapter = new Adaptador(adaptationProgress);

        String fileList = System.getProperty("user.home")
                + File.separator + "coruja_jlapsapi" + File.separator
                + "adaptacao" + File.separator + this.getName()
                + File.separator + "file.list";

        String wavsList = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileList));
            String str;
            int i = 0;
            while (br.ready() && i < 1) {
                str = br.readLine();
                wavsList = System.getProperty("user.home") + File.separator
                        + "coruja_jlapsapi" + File.separator + "adaptacao"
                        + File.separator + this.getName() + File.separator + str;
                i++;
            }

            br.close();
            int nRecords = countLines(wavsList);
            
            if (nRecords < texts.length - 1) {
                speakerAdaptationWindow.showInfoBoxMessage(
                        "Erro de adaptação",
                        "É necessário a gravação de todas as frases\nGravados:" + (nRecords + 1) + "/" + texts.length,
                        "errorbox");
                SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
                SpeechOO.logger.info("É necessário a gravação de todas as frases. Gravados:" + (nRecords + 1) + "/" + texts.length);
                return;
            }
        } catch (IOException ex) {
            SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
            SpeechOO.logger.error(ex);
        }

        adapter.startAdaptation(fileList, this.getName());
    }
}
