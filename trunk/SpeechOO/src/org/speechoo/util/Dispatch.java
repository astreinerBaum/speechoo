package org.speechoo.util;

// used interfaces
import com.sun.star.awt.XWindow;
import com.sun.star.frame.FeatureStateEvent;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import org.speechoo.SpeechOO;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XDispatchProviderInterception;
import com.sun.star.frame.XDispatchProviderInterceptor;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XStatusListener;
import com.sun.star.util.XURLTransformer;

public class Dispatch {

    private static XComponentContext xRemoteContext = null;
    private static XMultiComponentFactory xRemoteServiceManager = null;
    private static XURLTransformer xTransformer = null;
    public static com.sun.star.beans.PropertyValue[] lParams = new com.sun.star.beans.PropertyValue[1];
    private static Object m_desktop;
    private static XDesktop xdesktop;
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
     com.sun.star.frame.XFrames a = SpeechOO.m_xFrame.getCreator().getFrames();
     com.sun.star.frame.XFrame[] quadros =  a.queryFrames(com.sun.star.frame.FrameSearchFlag.ALL);
     System.out.println(quadros[0].getCreator());
     System.out.println(SpeechOO.m_xFrame.getCreator());
     //quadros[1].getContainerWindow().setVisible(false);
     m_desktop = SpeechOO.m_xContext.getServiceManager().createInstanceWithContext(
                    "com.sun.star.frame.Desktop", SpeechOO.m_xContext);
     xdesktop = (XDesktop) UnoRuntime.queryInterface(XDesktop.class, m_desktop);
     //System.out.println(xdesktop.getCurrentFrame().);
     //xChildContainer.append(xFrame) ;
    //xDispatchProvider.
    //xWindow.setVisible(false);
    }
}
    
