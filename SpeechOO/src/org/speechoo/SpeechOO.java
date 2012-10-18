/**
 * Copyright (C) 2010 SpeechOO Team (speechoo-dev AT googlegroups DOT com)
 *
 * SpeechOO (speechoo-dev AT googlegroups DOT com)
 *
 * CCSL-IME/USP (FLOSS Competence Center at IME - University of São Paulo),
 * Rua do Matão, 1010
 * CEP 05508-090 - São Paulo - SP - BRAZIL
 *
 * LAPS-UFPA (Signal Processing Laboratory - Federal University of Pará),
 * Rua Augusto Correa, 1
 * CEP 660750-110 - Belém - PA - Brazil
 *
 * http://code.google.com/p/speechoo
 *
 * This file is part of SpeechOO.
 *
 * SpeechOO is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpeechOO is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SpeechOO. If not, see <http://www.gnu.org/licenses/>.
 */
package org.speechoo;

import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.FeatureStateEvent;
import com.sun.star.frame.TerminationVetoException;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XTerminateListener;
import com.sun.star.lang.EventObject;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.AudioException;
import javax.swing.SwingConstants;
import javax.speech.Central;
import javax.speech.EngineStateError;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.RecognizerModeDesc;
import javax.speech.EngineException;
import javax.speech.recognition.DictationGrammar;
import javax.speech.recognition.Recognizer;
import javax.speech.recognition.RuleGrammar;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.speechoo.gui.SpeakerAdaptationDialog;
import org.speechoo.gui.TrainingDialog;

import org.speechoo.recognized.CommandsListener;
import org.speechoo.recognized.FreeDictationListener;
import org.speechoo.util.KeyEvent;
import org.speechoo.util.SpeechPropertiesCreator;
//import br.ufpa.laps.jlapsapi.recognizer.Recognizer;

/**
 * OOo Add-on entry point.
 *
 * @author William Colen
 */
public final class SpeechOO extends WeakBase
        implements com.sun.star.lang.XInitialization,
        com.sun.star.frame.XDispatch,
        com.sun.star.frame.XDispatchProvider,
        com.sun.star.lang.XServiceInfo,
        XTerminateListener {

    public static XComponentContext m_xContext;
    public static String extensionIdentifier = "org.speechoo.SpeechOO";
    public static com.sun.star.frame.XFrame m_xFrame;
    public static com.sun.star.frame.XFrame frame2;
    private static final String m_implementationName = SpeechOO.class.getName();
    private static final String[] m_serviceNames = {
        "com.sun.star.frame.ProtocolHandler"};
    boolean isResumed = false;
    private XPropertySet m_xDemoOptions = null;
    private boolean isActive = false;
    private boolean isInitialized = false;
    public static JFrame frame = new JFrame();
    public static JLabel label = new JLabel();
    public static Recognizer rec;
    public static RuleGrammar gram, gram2;
    public static DictationGrammar dic;
    private KeyEvent button;
    private SwingConstants Format;
   
    public SpeechOO(XComponentContext context) {
        System.out.println("SpeechOO SpeechOO");
        m_xContext = context;  

        frame.setFocusableWindowState(false); 
        frame.setVisible(false); 
        frame.setSize(200, 50);
	frame.setUndecorated(true); 
	frame.setLocation(850, 1000); 
        label.setFont(new Font("Serif",12, 12)); 
	label.setHorizontalAlignment(Format.CENTER); 
        label.setSize(20, 10); 
        frame.add(label); 
        }
        
    //
    public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
        System.out.println("SpeechOO __getComponenteFactory");
        XSingleComponentFactory xFactory = null;

        if (sImplementationName.equals(m_implementationName)) {
            xFactory = Factory.createComponentFactory(SpeechOO.class, m_serviceNames);
        }
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {
        System.out.println("SpeechOO __writeRegistryServiceInfo");
        return Factory.writeRegistryServiceInfo(m_implementationName,
                m_serviceNames,
                xRegistryKey);
    }

    // com.sun.star.lang.XInitialization:
    public void initialize(Object[] object)
            throws com.sun.star.uno.Exception {
        System.out.println("SpeechOO initialize []");
        if (object.length > 0) {
            m_xFrame = (com.sun.star.frame.XFrame) UnoRuntime.queryInterface(
                    com.sun.star.frame.XFrame.class, object[0]);
            
        }
    }

    @SuppressWarnings("static-access")
    private void initialize() throws Exception, BootstrapException{
        System.out.println("SpeechOO initialize");

  
        //facilita a configuração do plugin
        SpeechPropertiesCreator.create();
        //speech.properties é criado automaticamente na home do usuário
        
        RecognizerModeDesc rmd = (RecognizerModeDesc) Central.availableRecognizers(null).firstElement();
        System.out.println("RecognizerModeDesc");
        try {
            rec = (Recognizer) Central.createRecognizer(rmd);
            System.out.println("createRecognizer");
            label.setText("Criando Reconhecedor");
            frame.setVisible(true);
            rec.allocate();
            System.out.println("allocate");
            label.setText("Alocando");
            FileReader reader = new FileReader(System.getProperty("user.home") + "/coruja_jlapsapi/commands.grammar");
            //FileReader reader2 = new FileReader(System.getProperty("user.home") + "/coruja_jlapsapi/numbers.grammar");

            System.out.println("load gram");
            gram = rec.loadJSGF(reader);
            //gram2 = rec.loadJSGF(reader2);

            System.out.println("load dic");
            dic = rec.getDictationGrammar("dicSr");

            System.out.println("listeners");
            dic.addResultListener(new FreeDictationListener());
            gram.addResultListener(new CommandsListener());
            //gram2.addResultListener(new CommandsListener());
            gram.setEnabled(false);
            //gram2.setEnabled(false);
            button.begin();
           // try {
         //       Dispatch.dispatchCommand(".uno:Save");
        //    } catch (java.lang.Exception ex) {
        //        Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
        //    }

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (EngineException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (GrammarException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (EngineStateError ex) {
            ex.printStackTrace();
        }
    }

    // com.sun.star.frame.XDispatch:
    public void dispatch(com.sun.star.util.URL aURL,
            com.sun.star.beans.PropertyValue[] aArguments) {
        System.out.println("SpeechOO dispatch");
        if (aURL.Protocol.compareTo("org.speechoo.speechoo:") == 0) {
            if (aURL.Path.compareTo("startDictation") == 0) {
                synchronized (this) {
                    if (!this.isInitialized) {
                        try {
                            try {
                                initialize();
                            } catch (BootstrapException ex) {
                                Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println("initialize");
                        } catch (Exception ex) {
                            Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (this.isActive) {
                        try {
                            rec.deallocate();
                            System.out.println("deallocate");
                        } catch (EngineException ex) {
                            Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (EngineStateError ex) {
                            Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.isActive = false;
                    } else {
                        if (!isResumed) {
                            try {
                                rec.resume();
                                System.out.println("Resumed");
                                label.setText("Ativado");
                                frame.setVisible(true);
                            } catch (AudioException ex) {
                                Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (EngineStateError ex) {
                                Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            isResumed = true;
                            this.isInitialized=true;
                        } else {
                            rec.pause();
                            System.out.println("Paused");
                            label.setText("Pausado");
                            frame.setVisible(true);
                            isResumed = false;
                        }
                    }
                }
            } else if(aURL.Path.compareTo("speakerAdaptation") == 0) {
                SpeakerAdaptationDialog sad = new SpeakerAdaptationDialog();
                sad.show();
            }
        }
    }

    public void addStatusListener(com.sun.star.frame.XStatusListener xControl,
            com.sun.star.util.URL aURL) {
        System.out.println("SpeechOO addStatusListener");
        if (aURL.Path.compareTo("startDictation") == 0) {
            FeatureStateEvent aEvent = new FeatureStateEvent();
            aEvent.FeatureURL = aURL;
            aEvent.Source = (XDispatch) this;
            aEvent.IsEnabled = !this.isActive;
            aEvent.Requery = false;
            xControl.statusChanged(aEvent);
        } else if (aURL.Path.compareTo("speakerAdaptation") == 0) {
            FeatureStateEvent aEvent = new FeatureStateEvent();
            aEvent.FeatureURL = aURL;
            aEvent.Source = (XDispatch) this;
            aEvent.IsEnabled = !this.isActive;
            aEvent.Requery = false;
            xControl.statusChanged(aEvent);
        }
    }

    public void removeStatusListener(com.sun.star.frame.XStatusListener xControl,
            com.sun.star.util.URL aURL) {
        System.out.println("SpeechOO removeStatusListener");
        // add your own code here
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch queryDispatch(com.sun.star.util.URL aURL,
            String sTargetFrameName,
            int iSearchFlags) {
        System.out.println("SpeechOO queryDispatch");
        if (aURL.Protocol.compareTo("org.speechoo.speechoo:") == 0) {
            if (aURL.Path.compareTo("startDictation") == 0) {
                return this;
            } else if(aURL.Path.compareTo("speakerAdaptation") == 0){
                return this;
            }
        }
        return null;
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch[] queryDispatches(
            com.sun.star.frame.DispatchDescriptor[] seqDescriptors) {
        System.out.println("SpeechOO queryDispatches");
        int nCount = seqDescriptors.length;
        com.sun.star.frame.XDispatch[] seqDispatcher =
                new com.sun.star.frame.XDispatch[seqDescriptors.length];

        for (int i = 0; i < nCount; ++i) {
            seqDispatcher[i] = queryDispatch(seqDescriptors[i].FeatureURL,
                    seqDescriptors[i].FrameName,
                    seqDescriptors[i].SearchFlags);
        }
        return seqDispatcher;
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
        System.out.println("SpeechOO getImplementaionName");
        return m_implementationName;
    }

    public boolean supportsService(String sService) {
        System.out.println("SpeechOO supportsService");
        int len = m_serviceNames.length;

        for (int i = 0; i < len; i++) {
            if (sService.equals(m_serviceNames[i])) {
                return true;
            }
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        System.out.println("SpeechOO getSupportedServiceNames");
        return m_serviceNames;
    }

    /**
    private String getJuliusConfig() throws com.sun.star.uno.Exception {
    if (m_xDemoOptions == null) {
    initOptionsData();
    }

    return AnyConverter.toString(m_xDemoOptions.getPropertyValue("JConfiFile"));
    }

    private void initOptionsData() throws com.sun.star.uno.Exception {
    XMultiServiceFactory xConfig = (XMultiServiceFactory)
    UnoRuntime.queryInterface(XMultiServiceFactory.class,

    m_xContext.getServiceManager().createInstanceWithContext("com.sun.star.configuration.ConfigurationProvider",
    m_xContext));

    Object[] args = new Object[1];
    args[0] = new PropertyValue("nodepath", 0, "/org.speechoo.SpeechooOptions/SREngineOptions",
    PropertyState.DIRECT_VALUE);

    m_xDemoOptions = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class,
    xConfig.createInstanceWithArguments("com.sun.star.configuration.ConfigurationAccess",
    args));
    }

    private void showError(String errorMsg) {
    try {
    Dialog dialog = new Dialog(this.m_xContext, 100, 100, 50, 150, "SpeechOO Error", "errorDialog");
    dialog.insertFixedText(10, 10, 130, errorMsg);
    dialog.insertButton(60, 30, 30, "OK", PushButtonType.OK_value);
    short returnValue = dialog.execute();
    dialog.dispose();
    } catch (Exception ex) {
    Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
     */
    public static void main(String args[]) throws InterruptedException {
        System.out.println("SpeechOO main");
        Thread.sleep(50000);
    }
    private boolean terminated = false;

    private void terminate() {
        System.out.println("SpeechOO terminate");
        if (terminated == false) {
            terminated = true;
        }
    }

    // usage example:
    // http://wiki.services.openoffice.org/wiki/Documentation/DevGuide/OfficeDev/Using_the_Desktop
    public void queryTermination(EventObject arg0) throws TerminationVetoException {
        System.out.println("SpeechOO queryTermination");
        // nothing to do
    }

    public void notifyTermination(EventObject arg0) {
        System.out.println("SpeechOO notifyTermination");
        terminate();
    }

    public void disposing(EventObject arg0) {
        System.out.println("SpeechOO disposing");
    }
}
