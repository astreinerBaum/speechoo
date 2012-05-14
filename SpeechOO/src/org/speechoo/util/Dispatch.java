package org.speechoo.util;

// used interfaces
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import org.speechoo.SpeechOO;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.util.XURLTransformer;

public class Dispatch {

    private static XComponentContext xRemoteContext = null;
    private static XMultiComponentFactory xRemoteServiceManager = null;
    private static XURLTransformer xTransformer = null;
    public static com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[1];

    public static void dispatchCommand(String command) throws java.lang.Exception {

        com.sun.star.frame.XFrame xFrame = SpeechOO.m_xFrame.getController().getFrame();
        lParams[0] = new com.sun.star.beans.PropertyValue();
        lParams[0].Name = "nodepath";
        lParams[0].Value = "/org.openoffice.Office.Commands/Execute/Disabled";
        xRemoteContext = (XComponentContext) UnoRuntime.queryInterface(
                XComponentContext.class, SpeechOO.m_xContext);
        xRemoteServiceManager = xRemoteContext.getServiceManager();
        Object transformer = xRemoteServiceManager.createInstanceWithContext(
                "com.sun.star.util.URLTransformer", xRemoteContext);
        xTransformer = (com.sun.star.util.XURLTransformer) UnoRuntime.queryInterface(
                com.sun.star.util.XURLTransformer.class, transformer);
        XDispatchProvider xDispatchProvider = (com.sun.star.frame.XDispatchProvider) UnoRuntime.queryInterface(
                com.sun.star.frame.XDispatchProvider.class, xFrame);
        com.sun.star.util.URL[] aURL = new com.sun.star.util.URL[1];
        aURL[0] = new com.sun.star.util.URL();
        com.sun.star.frame.XDispatch xDispatch = null;
        aURL[0].Complete = command;
        xTransformer.parseSmart(aURL, ".uno");
        xDispatch = xDispatchProvider.queryDispatch(aURL[0], "", 0);
        if (xDispatch != null) {
            xDispatch.dispatch(aURL[0], lParams);
        }
    }
}
    
