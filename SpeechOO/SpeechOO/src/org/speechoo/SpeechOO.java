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

import com.sun.star.awt.PushButtonType;
import com.sun.star.awt.XTextComponent;
import com.sun.star.beans.PropertyState;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.deployment.PackageInformationProvider;
import com.sun.star.frame.FeatureStateEvent;
import com.sun.star.frame.TerminationVetoException;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XDispatch;
import com.sun.star.frame.XTerminateListener;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.Any;
import com.sun.star.uno.AnyConverter;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.speechoo.coruja.*;
import org.speechoo.gui.Dialog;

public final class SpeechOO extends WeakBase
        implements com.sun.star.lang.XInitialization,
        com.sun.star.frame.XDispatch,
        com.sun.star.frame.XDispatchProvider,
        com.sun.star.lang.XServiceInfo,
        XTerminateListener,
        Observer {

    private final XComponentContext m_xContext;
    private com.sun.star.frame.XFrame m_xFrame;
    private static final String m_implementationName = SpeechOO.class.getName();
    private static final String[] m_serviceNames = {
        "com.sun.star.frame.ProtocolHandler"};

    private XPropertySet m_xDemoOptions = null;

    private boolean isActive = false;
    private boolean isInitialized = false;

    public SpeechOO(XComponentContext context) {

        m_xContext = context;
        try {
            XMultiComponentFactory serviceManager = m_xContext.getServiceManager();
            // get Desktop instance
            Object desktop = serviceManager.createInstanceWithContext("com.sun.star.frame.Desktop", m_xContext);
            XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, desktop);
            xDesktop.addTerminateListener(this);
        } catch (Exception ex) {
            Logger.getLogger(SpeechOO.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public static XSingleComponentFactory __getComponentFactory(String sImplementationName) {
        XSingleComponentFactory xFactory = null;

        if (sImplementationName.equals(m_implementationName)) {
            xFactory = Factory.createComponentFactory(SpeechOO.class, m_serviceNames);
        }
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo(XRegistryKey xRegistryKey) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                m_serviceNames,
                xRegistryKey);
    }

    // com.sun.star.lang.XInitialization:
    public void initialize(Object[] object)
            throws com.sun.star.uno.Exception {
        if (object.length > 0) {
            m_xFrame = (com.sun.star.frame.XFrame) UnoRuntime.queryInterface(
                    com.sun.star.frame.XFrame.class, object[0]);
        }
    }

    // com.sun.star.frame.XDispatch:
    public void dispatch(com.sun.star.util.URL aURL,
            com.sun.star.beans.PropertyValue[] aArguments) {
        if (aURL.Protocol.compareTo("org.speechoo.speechoo:") == 0) {
            if (aURL.Path.compareTo("startDictation") == 0) {
                synchronized (this) {
                    try {
                        if(!this.isInitialized) {
                            // check configuration
                            String jconfig = getJuliusConfig();
                            File f = new File(jconfig);
                            if(!f.exists()) {
                                showError("Couldn't find Julius configuration.");
                                return;
                            }
                            String oxtRoot = PackageInformationProvider.get(m_xContext).getPackageLocation("org.speechoo.SpeechOO");
                            oxtRoot = oxtRoot.substring(7);
                            CorujaJNI.init(oxtRoot);
                            CorujaJNI.getSingleton().addObserver(this);
                            this.isInitialized = true;
                        }
                        if (this.isActive) {
                            CorujaJNI.getSingleton().dictation(false);
                            this.isActive = false;
                        } else {
                            CorujaJNI.getSingleton().startSpeechRecognitionEngine(getJuliusConfig());
                            CorujaJNI.getSingleton().dictation(true);
                            this.isActive = true;
                        }
                    } catch (com.sun.star.uno.Exception e) {
                        // TODO: improved error handling;
                        throw new com.sun.star.lang.WrappedTargetRuntimeException("wrapped UNO exception",
                                this, new Any(new com.sun.star.uno.Type(Exception.class), e));
                    }
                }

            }
        }
    }

    public void addStatusListener(com.sun.star.frame.XStatusListener xControl,
            com.sun.star.util.URL aURL) {
        if (aURL.Path.compareTo("startDictation") == 0) {
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
        // add your own code here
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch queryDispatch(com.sun.star.util.URL aURL,
            String sTargetFrameName,
            int iSearchFlags) {
        if (aURL.Protocol.compareTo("org.speechoo.speechoo:") == 0) {
            if (aURL.Path.compareTo("startDictation") == 0) {
                return this;
            }
        }
        return null;
    }

    // com.sun.star.frame.XDispatchProvider:
    public com.sun.star.frame.XDispatch[] queryDispatches(
            com.sun.star.frame.DispatchDescriptor[] seqDescriptors) {
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
        return m_implementationName;
    }

    public boolean supportsService(String sService) {
        int len = m_serviceNames.length;

        for (int i = 0; i < len; i++) {
            if (sService.equals(m_serviceNames[i])) {
                return true;
            }
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    public void insertNewSentence(String sentence) {
                         try {
                     XTextDocument xDoc = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class,
                             m_xFrame.getController().getModel());
                     XText xText = xDoc.getText();
                     XTextCursor xCursor = xText.createTextCursor();
                     xCursor.gotoEnd(false);
                     xText.insertString(xCursor, sentence, true);
                     xCursor.gotoEnd(false);
                     xText.insertControlCharacter(xCursor, ControlCharacter.PARAGRAPH_BREAK, false);
                 } catch (com.sun.star.uno.Exception e) {
                     // TODO: improved error handling;
                     throw new com.sun.star.lang.WrappedTargetRuntimeException("wrapped UNO exception",
			      this, new Any(new com.sun.star.uno.Type(Exception.class), e));
                 }

    }

    public void update(Observable o, Object arg) {

        if (arg instanceof String) {
            insertNewSentence((String) arg);
        }

    }
    

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

    public static void main(String args[]) throws InterruptedException {

        Observer observer = new Observer() {

            public void update(Observable o, Object arg) {
                if (arg instanceof String) {
                    System.out.println((String) arg);
                }
            }
        };

        String path = "/home/colen/NetBeansProjects/CorujaJNI/native/linux/target";
        //path = path.substring(7);

        CorujaJNI.init(path);
        CorujaJNI.getSingleton().addObserver(observer);
        CorujaJNI.getSingleton().dictation(true);

        Thread.sleep(50000);
    }

    private void terminate() {
        CorujaJNI.getSingleton().stopSpeechRecognitionEngine();
    }

    // usage example:
    // http://wiki.services.openoffice.org/wiki/Documentation/DevGuide/OfficeDev/Using_the_Desktop
    public void queryTermination(EventObject arg0) throws TerminationVetoException {
        // nothing to do
    }

    public void notifyTermination(EventObject arg0) {
        terminate();
    }

    public void disposing(EventObject arg0) {
        terminate();
    }



}
