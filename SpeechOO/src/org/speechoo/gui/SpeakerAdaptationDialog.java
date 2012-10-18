package org.speechoo.gui;

import com.sun.star.awt.ItemEvent;
import com.sun.star.awt.PushButtonType;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XButton;
import com.sun.star.awt.XComboBox;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XItemListener;
import com.sun.star.awt.XTextComponent;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.SpeechOO;
import org.speechoo.util.AcousticModelSelector;

/**
 *
 * @author Hugo Santos
 */
public class SpeakerAdaptationDialog extends Thread {

    private Dialog modelSelectorWindow;
    private XComboBox xComboBox;
    private XButton xbSelect, xbCreate, xbAdapt;
    private XTextComponent xTextComponent;
    private String modelName;
    private String[] adaptProfileList;
    private String adaptationPath = System.getProperty("user.home") + File.separator
            + "coruja_jlapsapi" + File.separator + "adaptacao";
    private Boolean existingAdaptedProfileList[];
    private XPropertySet xSelectButtonPropertySet, xAdaptButtonPropertySet;

    public SpeakerAdaptationDialog() {
        File dir = new File(adaptationPath);
        String tmp[] = dir.list();
        File ftmp;
        adaptProfileList = new String[tmp.length - 1];
        existingAdaptedProfileList = new Boolean[tmp.length - 1];
        int j = 0;
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals("labs")) { //labs folder doesn't have acoustic models
                continue;
            }
            ftmp = new File(adaptationPath + File.separator + tmp[i] + File.separator + tmp[i] + ".am");
            if (ftmp.exists()) {
                existingAdaptedProfileList[j] = true;
            } else if (!ftmp.exists()) {
                existingAdaptedProfileList[j] = false;
            }
            adaptProfileList[j++] = tmp[i];
            if (j == tmp.length - 1) {
                break; //keep j inside of adaptProfileList lenght
            }

        }
    }

    public void show() {
        try {

            modelSelectorWindow = new Dialog(SpeechOO.m_xContext, 420, 420,
                    150, 190, "SpeechOO - Adaptação de locutor", "");

            //############################# linha 1 ############################
            modelSelectorWindow.insertFixedText(10, 15, 12, 100, "Criação de um novo modelo adaptado");
            xTextComponent = modelSelectorWindow.insertTextField(10, 35, 120, 12, "", false, false);
            xTextComponent.setText("Digite o nome do seu novo modelo");
            modelSelectorWindow.insertButton(140, 35, 35, "Criar", PushButtonType.STANDARD_value, true, "createButton");
            XControl xButtonControlCreate = modelSelectorWindow.getxDialogContainer()
                    .getControl("createButton");
            xbCreate = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlCreate);
            xbCreate.addActionListener(new XActionListener() {
                public void actionPerformed(com.sun.star.awt.ActionEvent arg0) {
                    try {
                        modelName = xTextComponent.getText();
                        if ("".equals(modelName) || "Digite o nome do seu novo modelo".equals(modelName)) {
                            modelSelectorWindow.insertFixedText(10, 45, 12, 100, "Nome inválido");
                        }
                        for (int i = 0; i < adaptProfileList.length; i++) {
                            if (modelName == null ? adaptProfileList[i] == null : modelName.equals(adaptProfileList[i])) {
                                modelSelectorWindow.insertFixedText(10, 45, 12, 100, "Nome já existente, digite outro");
                                break;
                            }
                        }

                        File file;
                        file = new File(adaptationPath + File.separator + modelName);
                        if (!file.mkdir()) {
                            System.out.println("Não criou:"+adaptationPath + File.separator + modelName);
                        }
                        file = new File(adaptationPath + File.separator + modelName + File.separator + "records");
                        if (!file.mkdir()) {
                            System.out.println("Não criou: "+adaptationPath + File.separator + modelName + File.separator + "records");
                        }
                        BufferedWriter fileList;
                        fileList = new BufferedWriter(new FileWriter(adaptationPath+File.separator+modelName+File.separator+"file.list"));
                        fileList.write("wav_mfc_adapt.list\n");
                        fileList.write("pattern.list\n");
                        fileList.write("lab_adapt.list\n");
                        fileList.write("../LaPSAM-1.5/tiedlist\n");
                        fileList.write("../LaPSAM-1.5/MMF\n");
                        fileList.write(modelName+".am");
                        fileList.close();
                        modelSelectorWindow.close();
                        TrainingDialog td = new TrainingDialog();
                        td.train(modelName);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject eo) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            modelSelectorWindow.insertFixedText(10, 60, 12, 180, ".......................................................................................................");

            //###################### linha 2 ###################################
            modelSelectorWindow.insertFixedText(10, 80, 12, 160, "Selecione um modelo já existente para adaptar ou utilizar");
            xComboBox = modelSelectorWindow.insertComboBox(10, 100, 75, adaptProfileList, "xComboBox", "Selecione um modelo");
            xComboBox.addItemListener(new XItemListener() {
                public void itemStateChanged(ItemEvent arg0) {
                    try {
                        modelName = xComboBox.getItem((short) arg0.Selected);
                        if (modelName.equals("LaPSAM-1.5")) {
                            xAdaptButtonPropertySet.setPropertyValue("Enabled", false);
                        } else {
                            xAdaptButtonPropertySet.setPropertyValue("Enabled", true);
                        }
                        if (existingAdaptedProfileList[arg0.Selected]) {
                            xSelectButtonPropertySet.setPropertyValue("Enabled", true);
                        } else {
                            xSelectButtonPropertySet.setPropertyValue("Enabled", false);
                        }
                    } catch (UnknownPropertyException ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (WrappedTargetException ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject eo) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            Object adaptButton = modelSelectorWindow.insertButton(100, 100, 30, "Adaptar", PushButtonType.STANDARD_value, true, "adaptButton");
            xAdaptButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, adaptButton);
            XControl xButtonControlAdaptButton = modelSelectorWindow.getxDialogContainer()
                    .getControl("adaptButton");
            xbAdapt = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlAdaptButton);
            xbAdapt.addActionListener(new XActionListener() {
                public void actionPerformed(com.sun.star.awt.ActionEvent arg0) {
                    try {
                        if (modelName == null || "".equals(modelName) || "Digite o nome do seu novo modelo".equals(modelName)) {
                            modelSelectorWindow.insertFixedText(10, 110, 12, 100, "Selecione um modelo primeiro");
                        } else {
                            TrainingDialog td = new TrainingDialog();
                            td.train(modelName);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject eo) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            Object SelectButton = modelSelectorWindow.insertButton(140, 100, 35, "Selecionar", PushButtonType.STANDARD_value, false, "selectButton");
            xSelectButtonPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, SelectButton);
            XControl xButtonControlTopOk = modelSelectorWindow.getxDialogContainer()
                    .getControl("selectButton");
            xbSelect = (XButton) UnoRuntime.queryInterface(XButton.class, xButtonControlTopOk);
            xbSelect.addActionListener(new XActionListener() {
                public void actionPerformed(com.sun.star.awt.ActionEvent arg0) {
                    try {
                        if (modelName == null || "".equals(modelName) || "Digite o nome do seu novo modelo".equals(modelName)) {
                            modelSelectorWindow.insertFixedText(10, 110, 12, 100, "Selecione um modelo primeiro");
                        } else {
                            AcousticModelSelector.changeText("-h adaptacao/" + modelName + "/" + modelName + ".am");
                            modelSelectorWindow.showInfoBoxMessage("Seleção de Modelo", "Modelo " + modelName + " selecionado com sucesso!");
                            modelSelectorWindow.close();
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                public void disposing(EventObject eo) {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });

            modelSelectorWindow.execute();
            modelSelectorWindow.dispose();

        } catch (Exception ex) {
            Logger.getLogger(SpeakerAdaptationDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
