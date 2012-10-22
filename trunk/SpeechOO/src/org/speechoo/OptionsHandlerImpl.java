package org.speechoo;

import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.beans.PropertyState;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.lang.XSingleComponentFactory;
import com.sun.star.lib.uno.helper.Factory;
import com.sun.star.lib.uno.helper.WeakBase;
import com.sun.star.registry.XRegistryKey;
import com.sun.star.uno.Any;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XChangesBatch;


public final class OptionsHandlerImpl extends WeakBase
   implements com.sun.star.lang.XServiceInfo,
              com.sun.star.awt.XContainerWindowEventHandler
{
    private final XComponentContext m_xContext;
    private static final String m_implementationName = OptionsHandlerImpl.class.getName();
    private static final String[] m_serviceNames = {
        "org.speechoo.OptionsHandler" };

     private final XMultiComponentFactory m_xMCF;
     private final XPropertySet m_xPropOptions;

    public OptionsHandlerImpl( XComponentContext context )
    {
         m_xContext = context;
         m_xMCF = m_xContext.getServiceManager();

         XMultiServiceFactory xConfig;
         try {
             xConfig = (XMultiServiceFactory) UnoRuntime.queryInterface(XMultiServiceFactory.class,
                     m_xMCF.createInstanceWithContext("com.sun.star.configuration.ConfigurationProvider", m_xContext));

             Object[] args = new Object[1];
             args[0] = new PropertyValue("nodepath", 0, "/org.speechoo.SpeechooOptions/SREngineOptions",
                     PropertyState.DIRECT_VALUE);

             m_xPropOptions = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class,
                     xConfig.createInstanceWithArguments("com.sun.star.configuration.ConfigurationUpdateAccess", args));
         } catch (com.sun.star.uno.Exception e) {
             // TODO: improved error handling;
             throw new com.sun.star.lang.WrappedTargetRuntimeException("wrapped UNO exception", this,
                     new Any(new com.sun.star.uno.Type(Exception.class), e));
         }
    };

    public String[] getSupportedMethodNames() {
         return new String[]{"external_event"};
     }

         // com.sun.star.awt.XContainerWindowEventHandler:
     public boolean callHandlerMethod(com.sun.star.awt.XWindow xWindow, Object aEventObject, String sMethodName) throws
     com.sun.star.lang.WrappedTargetException {
          if (sMethodName.equals("external_event")) {
              try {
                  return handleExternalEvent(xWindow, aEventObject);
              } catch (com.sun.star.uno.RuntimeException re) {
                  throw re;
              } catch (com.sun.star.uno.Exception e) {
                  throw new WrappedTargetException(sMethodName, this, e);
              }
          }
         return true;
     }

     private boolean handleExternalEvent(com.sun.star.awt.XWindow aWindow, Object aEventObject) throws
     com.sun.star.uno.Exception {
         try {
             String sMethod = AnyConverter.toString(aEventObject);

             if (sMethod.equals("ok")) {
                 saveData(aWindow);
             } else if (sMethod.equals("back") || sMethod.equals("initialize")) {
                 loadData(aWindow);
             }
         } catch (com.sun.star.lang.IllegalArgumentException e) {
             throw new com.sun.star.lang.IllegalArgumentException(
                     "Method external_event requires a string in the event object argument.",
                     this, (short) -1);
         }
         return true;
     }

     private void saveData(com.sun.star.awt.XWindow aWindow)
             throws com.sun.star.lang.IllegalArgumentException, com.sun.star.uno.Exception {

         //To access the controls of the window we need to obtain the
         //XControlContainer from the window implementation
         XControlContainer xContainer = (XControlContainer) UnoRuntime.queryInterface(XControlContainer.class, aWindow);
         if (xContainer == null) {
             throw new com.sun.star.uno.Exception("Could not get XControlContainer from window.", this);
         }

         XControl xControl = xContainer.getControl("ConfigFile");
         if (xControl != null) {
             XPropertySet xProp = (XPropertySet) UnoRuntime.queryInterface(
                     XPropertySet.class, xControl.getModel());

             Object aText = xProp.getPropertyValue("Text");
             m_xPropOptions.setPropertyValue("JConfiFile", AnyConverter.toString(aText));
         }

         //Committing the changes will cause or changes to be written to the registry.
         XChangesBatch xUpdateCommit = (XChangesBatch) UnoRuntime.queryInterface(XChangesBatch.class, m_xPropOptions);
         xUpdateCommit.commitChanges();
     }
     private void loadData(com.sun.star.awt.XWindow aWindow)
             throws com.sun.star.uno.Exception {

         //To acces the controls of the window we need to obtain the
         //XControlContainer from window implementation
         XControlContainer xContainer = (XControlContainer) UnoRuntime.queryInterface(XControlContainer.class, aWindow);
         if (xContainer == null) {
             throw new com.sun.star.uno.Exception("Could not get XControlContainer from window.", this);
         }


         XControl xControl = xContainer.getControl("ConfigFile");
         if (xControl != null) {
             Object aValue = m_xPropOptions.getPropertyValue("JConfiFile");

             XPropertySet xProp = (XPropertySet) UnoRuntime.queryInterface(
                     XPropertySet.class, xControl.getModel());

             xProp.setPropertyValue("Text", aValue);
         }
     }
 


    public static XSingleComponentFactory __getComponentFactory( String sImplementationName ) {
        XSingleComponentFactory xFactory = null;

        if ( sImplementationName.equals( m_implementationName ) )
            xFactory = Factory.createComponentFactory(OptionsHandlerImpl.class, m_serviceNames);
        return xFactory;
    }

    public static boolean __writeRegistryServiceInfo( XRegistryKey xRegistryKey ) {
        return Factory.writeRegistryServiceInfo(m_implementationName,
                                                m_serviceNames,
                                                xRegistryKey);
    }

    // com.sun.star.lang.XServiceInfo:
    public String getImplementationName() {
         return m_implementationName;
    }

    public boolean supportsService( String sService ) {
        int len = m_serviceNames.length;

        for( int i=0; i < len; i++) {
            if (sService.equals(m_serviceNames[i]))
                return true;
        }
        return false;
    }

    public String[] getSupportedServiceNames() {
        return m_serviceNames;
    }

    // com.sun.star.awt.XContainerWindowEventHandler:


}
