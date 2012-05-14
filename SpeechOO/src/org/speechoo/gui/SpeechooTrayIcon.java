package org.speechoo.gui;

/**
 *
 * @author 10080000701
 */

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.AudioException;
import javax.speech.EngineStateError;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.speechoo.SpeechOO;

public class SpeechooTrayIcon {

	static String status="variável";
	static TrayIcon trayIcon = new TrayIcon(createImage("images/gnome-sound-recorder_26_loading.png", "tray icon"));
    public static void main(String[] args) {
        /* Select an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public void load() {
        /* Select an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();

        final SystemTray tray = SystemTray.getSystemTray();
        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("Sobre");
        MenuItem startRecognition = new MenuItem("Começar Reconhecimento");
        MenuItem pauseRecognition = new MenuItem("Pausar Reconhecimento");
        Menu switchMode = new Menu("Mudar de modo");
        MenuItem dictationMode = new MenuItem("Modo Ditado");
        MenuItem commandMode = new MenuItem("Modo Comando");
        MenuItem adaptation = new MenuItem("Adaptação à voz");
        CheckboxMenuItem enableTooltip = new CheckboxMenuItem("ativar tooltip");
//        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
//        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
//        Menu displayMenu = new Menu("Display");
//        MenuItem errorItem = new MenuItem("Error");
//        MenuItem warningItem = new MenuItem("Warning");
//        MenuItem infoItem = new MenuItem("Info");
//        MenuItem noneItem = new MenuItem("None");
//        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(startRecognition);
        popup.add(pauseRecognition);
        popup.addSeparator();
        popup.add(switchMode);
        switchMode.add(dictationMode);
        switchMode.add(commandMode);
        popup.add(adaptation);
        popup.addSeparator();
        popup.add(enableTooltip);
        popup.add(aboutItem);
//        popup.add(displayMenu);
//        displayMenu.add(errorItem);
//        displayMenu.add(warningItem);
//        displayMenu.add(infoItem);
//        displayMenu.add(noneItem);
//        popup.add(exitItem);
        trayIcon.setImageAutoSize(true);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Ícone de status do SpeechOO");
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Versão 1.1.x");
            }
        });
/*
        cb1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb1Id = e.getStateChange();
                if (cb1Id == ItemEvent.SELECTED){
                    trayIcon.setImageAutoSize(true);
                } else {
                    trayIcon.setImageAutoSize(false);
                }
            }
        });
 */     //enableTooltip.dispatchEvent(new ItemEvent(source, id, item, stateChange));
        enableTooltip.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int cb2Id = e.getStateChange();
                if (cb2Id == ItemEvent.SELECTED){
                    trayIcon.setToolTip(SpeechooTrayIcon.status);
                } else {
                    trayIcon.setToolTip(null);
                }
            }
        });

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem)e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Adaptação à voz".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("SpeechOO", "tela adaptação ao locutor", TrayIcon.MessageType.NONE);
                } else if ("Começar Reconhecimento".equals(item.getLabel())) {
                    try {
                        SpeechOO.rec.resume();
                    } catch (AudioException ex) {
                        Logger.getLogger(SpeechooTrayIcon.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (EngineStateError ex) {
                        Logger.getLogger(SpeechooTrayIcon.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    resumed();
                } else if ("Pausar Reconhecimento".equals(item.getLabel())) {
                    SpeechOO.rec.pause();
                    paused();
                }
            }
        };
        adaptation.addActionListener(listener);
        startRecognition.addActionListener(listener);
        pauseRecognition.addActionListener(listener);
//        exitItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                tray.remove(trayIcon);
//                System.exit(0);
//            }
//        });
    }

    //Obtain the image URL
    protected static Image createImage(String path, String description) {
            return (new ImageIcon(path, description)).getImage();
    }

    public static void loading(){
        trayIcon.setImage(createImage(System.getProperty("user.home")+"images/gnome-sound-recorder_26_loading.png", "começou"));
    }

    public static void resumed(){
        trayIcon.setImage(createImage(System.getProperty("user.home")+"images/gnome-sound-recorder_26.png", "começou"));
    }

    public static void paused(){
        trayIcon.setImage(createImage(System.getProperty("user.home")+"images/gnome-sound-recorder_26_paused.png", "começou"));
    }
}
