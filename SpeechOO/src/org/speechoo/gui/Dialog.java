/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.gui;

import com.sun.star.awt.Rectangle;
import com.sun.star.awt.XComboBox;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XDialog;
import com.sun.star.awt.XFixedText;
import com.sun.star.awt.XMessageBox;
import com.sun.star.awt.XMessageBoxFactory;
import com.sun.star.awt.XTextComponent;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNameContainer;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import org.speechoo.SpeechOO;

/**
 * The source code of this class bases heavily on the OOo Developer's Guide:
 * http://api.openoffice.org/docs/DevelopersGuide/GUI/GUI.xhtml#1_Graphical_User_Interfaces
 *
 * This class provides methods to create, execute and dispose an OOo Dialog with
 * labels (fixed text), buttons, text fields, and password fields.
 */
/**
 *
 * @author Hugo Santos
 */
public class Dialog {

    private XComponentContext xComponentContext;
    private XMultiComponentFactory xMultiComponentFactory;
    private XMultiServiceFactory xMultiComponentFactoryDialogModel;
    private XNameContainer xDialogModelNameContainer;
    private XControl xDialogControl;
    private XControlContainer xDialogContainer;

    public XComponentContext getxComponentContext() {
        return xComponentContext;
    }

    public XControlContainer getxDialogContainer() {
        return xDialogContainer;
    }

    public XControl getxDialogControl() {
        return xDialogControl;
    }

    public XNameContainer getxDialogModelNameContainer() {
        return xDialogModelNameContainer;
    }

    public XMultiComponentFactory getxMultiComponentFactory() {
        return xMultiComponentFactory;
    }

    public XMultiServiceFactory getxMultiComponentFactoryDialogModel() {
        return xMultiComponentFactoryDialogModel;
    }

    public Dialog(XComponentContext xComponentContext, int posX, int posY, int height, int width, String title, String name) throws Exception {

        this.xComponentContext = xComponentContext;
        init(posX, posY, height, width, title, name);
    }

    private void init(int posX, int posY, int height, int width, String title, String name) throws Exception {

        xMultiComponentFactory = xComponentContext.getServiceManager();

        Object oDialogModel = xMultiComponentFactory.createInstanceWithContext("com.sun.star.awt.UnoControlDialogModel", xComponentContext);

        // The XMultiServiceFactory of the dialogmodel is needed to instantiate the controls...
        xMultiComponentFactoryDialogModel = (XMultiServiceFactory) UnoRuntime
                .queryInterface(XMultiServiceFactory.class, oDialogModel);

        // The named container is used to insert the created controls into...
        xDialogModelNameContainer = (XNameContainer) UnoRuntime.queryInterface(XNameContainer.class, oDialogModel);

        // create the dialog...
        Object oUnoDialog = xMultiComponentFactory.createInstanceWithContext("com.sun.star.awt.UnoControlDialog", xComponentContext);
        xDialogControl = (XControl) UnoRuntime.queryInterface(XControl.class, oUnoDialog);

        // The scope of the control container is public...
        xDialogContainer = (XControlContainer) UnoRuntime.queryInterface(XControlContainer.class, oUnoDialog);

        // link the dialog and its model...
        XControlModel xControlModel = (XControlModel) UnoRuntime.queryInterface(XControlModel.class, oDialogModel);
        xDialogControl.setModel(xControlModel);

        // Create a unique name
        String uniqueName = createUniqueName(xDialogModelNameContainer, (name != null) ? name : "LiboDialog");

        // Define the dialog at the model
        XPropertySet xDialogPropertySet = (XPropertySet) UnoRuntime
                .queryInterface(XPropertySet.class, xDialogModelNameContainer);
        xDialogPropertySet.setPropertyValue("PositionX", new Integer(posX));
        xDialogPropertySet.setPropertyValue("PositionY", new Integer(posY));
        xDialogPropertySet.setPropertyValue("Height", new Integer(height));
        xDialogPropertySet.setPropertyValue("Width", new Integer(width));
        xDialogPropertySet.setPropertyValue("Title", (title != null) ? title : "LibreOffice.org Dialog");
        xDialogPropertySet.setPropertyValue("Name", uniqueName);
        xDialogPropertySet.setPropertyValue("Moveable", Boolean.TRUE);
        xDialogPropertySet.setPropertyValue("TabIndex", new Short((short) 0));
    }

    public short execute() throws Exception {

        XWindow xWindow = (XWindow) UnoRuntime.queryInterface(XWindow.class, xDialogContainer);

        // set the dialog invisible until it is executed
        xWindow.setVisible(false);

        Object oToolkit = xMultiComponentFactory.createInstanceWithContext("com.sun.star.awt.Toolkit", xComponentContext);
        XWindowPeer xWindowParentPeer = ((XToolkit) UnoRuntime.queryInterface(XToolkit.class, oToolkit)).getDesktopWindow();
        XToolkit xToolkit = (XToolkit) UnoRuntime.queryInterface(XToolkit.class, oToolkit);
        xDialogControl.createPeer(xToolkit, xWindowParentPeer);

        // the return value contains information about how the dialog has been closed
        XDialog xDialog = (XDialog) UnoRuntime.queryInterface(XDialog.class, xDialogControl);
        return xDialog.execute();
    }

    public void close() throws Exception {

        XWindow xWindow = (XWindow) UnoRuntime.queryInterface(XWindow.class, xDialogContainer);

        // set the dialog invisible until it is executed
        xWindow.setVisible(false);

        Object oToolkit = xMultiComponentFactory.createInstanceWithContext("com.sun.star.awt.Toolkit", xComponentContext);
        XWindowPeer xWindowParentPeer = ((XToolkit) UnoRuntime.queryInterface(XToolkit.class, oToolkit)).getDesktopWindow();
        XToolkit xToolkit = (XToolkit) UnoRuntime.queryInterface(XToolkit.class, oToolkit);
        xDialogControl.createPeer(xToolkit, xWindowParentPeer);

        // the return value contains information about how the dialog has been closed
        XDialog xDialog = (XDialog) UnoRuntime.queryInterface(XDialog.class, xDialogControl);
        xDialog.endExecute();
    }

    public void dispose() {

        // Free the resources
        XComponent xDialogComponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, xDialogControl);
        xDialogComponent.dispose();
    }

    public XFixedText insertFixedText(int posX, int posY, int height, int width, String label) throws Exception {

        // Create a unique name
        String uniqueName = createUniqueName(xDialogModelNameContainer, "FixedText");

        // Create a fixed text control model
        Object oFixedTextModel = xMultiComponentFactoryDialogModel.createInstance("com.sun.star.awt.UnoControlFixedTextModel");

        // Set the properties at the model
        XPropertySet xFixedTextPropertySet = (XPropertySet) UnoRuntime
                .queryInterface(XPropertySet.class, oFixedTextModel);
        xFixedTextPropertySet.setPropertyValue("PositionX", new Integer(posX));
        xFixedTextPropertySet.setPropertyValue("PositionY", new Integer(posY + 2));
        xFixedTextPropertySet.setPropertyValue("Height", new Integer(height));
        xFixedTextPropertySet.setPropertyValue("Width", new Integer(width));
        xFixedTextPropertySet.setPropertyValue("Label", (label != null) ? label : "");
        xFixedTextPropertySet.setPropertyValue("Name", uniqueName);

        // Add the model to the dialog model name container
        xDialogModelNameContainer.insertByName(uniqueName, oFixedTextModel);

        // Reference the control by the unique name
        XControl xFixedTextControl = xDialogContainer.getControl(uniqueName);

        return (XFixedText) UnoRuntime.queryInterface(XFixedText.class, xFixedTextControl);
    }

    public Object insertButton(int posX, int posY, int width, String label, int pushButtonType, boolean enabled, String uniqueName) throws Exception {

        // Create a button control model
        Object oButtonModel = xMultiComponentFactoryDialogModel.createInstance("com.sun.star.awt.UnoControlButtonModel");

        // Set the properties at the model
        XPropertySet xButtonPropertySet = (XPropertySet) UnoRuntime
                .queryInterface(XPropertySet.class, oButtonModel);
        xButtonPropertySet.setPropertyValue("PositionX", new Integer(posX));
        xButtonPropertySet.setPropertyValue("PositionY", new Integer(posY));
        xButtonPropertySet.setPropertyValue("Height", new Integer(14));
        xButtonPropertySet.setPropertyValue("Width", new Integer(width));
        xButtonPropertySet.setPropertyValue("Label", (label != null) ? label : "");
        xButtonPropertySet.setPropertyValue("PushButtonType", new Short((short) pushButtonType));
        xButtonPropertySet.setPropertyValue("Name", uniqueName);
        xButtonPropertySet.setPropertyValue("Enabled", enabled);

        // Add the model to the dialog model name container
        xDialogModelNameContainer.insertByName(uniqueName, oButtonModel);

        return oButtonModel;
    }

    public XTextComponent insertTextField(int posX, int posY, int width, int height, String text, boolean multiLine, boolean readOnly) throws Exception {
        return insertEditableTextField(posX, posY, width, height, text, ' ', multiLine, readOnly);

    }

    public XTextComponent insertPasswordField(int posX, int posY, int width, int height, String text, char echoChar, boolean multiLine, boolean readOnly) throws Exception {

        return insertEditableTextField(posX, posY, width, height, text, echoChar, multiLine, readOnly);
    }

    private XTextComponent insertEditableTextField(int posX, int posY, int width, int height, String text, char echoChar, boolean multiLine, boolean readOnly) throws Exception {

        // Create a unique name
        String uniqueName = createUniqueName(xDialogModelNameContainer, "EditableTextField");

        // Create an editable text field control model
        Object oEditableTextFieldModel = xMultiComponentFactoryDialogModel
                .createInstance("com.sun.star.awt.UnoControlEditModel");

        // Set the properties at the model
        XPropertySet xEditableTextFieldPropertySet = (XPropertySet) UnoRuntime
                .queryInterface(XPropertySet.class, oEditableTextFieldModel);
        xEditableTextFieldPropertySet.setPropertyValue("PositionX", new Integer(posX));
        xEditableTextFieldPropertySet.setPropertyValue("PositionY", new Integer(posY));
        xEditableTextFieldPropertySet.setPropertyValue("Height", new Integer(height));
        xEditableTextFieldPropertySet.setPropertyValue("Width", new Integer(width));
        xEditableTextFieldPropertySet.setPropertyValue("Text", (text != null) ? text : "");
        xEditableTextFieldPropertySet.setPropertyValue("Name", uniqueName);
        xEditableTextFieldPropertySet.setPropertyValue("MultiLine", multiLine);
        xEditableTextFieldPropertySet.setPropertyValue("ReadOnly", readOnly);

        if (echoChar != 0 && echoChar != ' ') {
            // Useful for password fields
            xEditableTextFieldPropertySet.setPropertyValue("EchoChar", new Short((short) echoChar));
        }

        // Add the model to the dialog model name container
        xDialogModelNameContainer.insertByName(uniqueName, oEditableTextFieldModel);

        // Reference the control by the unique name
        XControl xEditableTextFieldControl = xDialogContainer.getControl(uniqueName);

        return (XTextComponent) UnoRuntime.queryInterface(XTextComponent.class, xEditableTextFieldControl);
    }

    private XTextComponent insertTextField(int posX, int posY, int width, int height, String text, char echoChar) throws Exception {

        // Create a unique name
        String uniqueName = createUniqueName(xDialogModelNameContainer, "TextField");

        // Create an editable text field control model
        Object oTextFieldModel = xMultiComponentFactoryDialogModel.createInstance("com.sun.star.awt.UnoControlEditModel");

        // Set the properties at the model
        XPropertySet xTextFieldPropertySet = (XPropertySet) UnoRuntime
                .queryInterface(XPropertySet.class, oTextFieldModel);
        xTextFieldPropertySet.setPropertyValue("PositionX", new Integer(posX));
        xTextFieldPropertySet.setPropertyValue("PositionY", new Integer(posY));
        xTextFieldPropertySet.setPropertyValue("Height", new Integer(height));
        xTextFieldPropertySet.setPropertyValue("Width", new Integer(width));
        xTextFieldPropertySet.setPropertyValue("Text", (text != null) ? text : "");
        xTextFieldPropertySet.setPropertyValue("Name", uniqueName);
        xTextFieldPropertySet.setPropertyValue("MultiLine", false);
        xTextFieldPropertySet.setPropertyValue("ReadOnly", false);

        if (echoChar != 0 && echoChar != ' ') {
            // Useful for password fields
            xTextFieldPropertySet.setPropertyValue("EchoChar", new Short((short) echoChar));
        }

        // Add the model to the dialog model name container
        xDialogModelNameContainer.insertByName(uniqueName, oTextFieldModel);

        // Reference the control by the unique name
        XControl xTextFieldControl = xDialogContainer.getControl(uniqueName);

        return (XTextComponent) UnoRuntime.queryInterface(XTextComponent.class, xTextFieldControl);
    }

    /**
     * Makes a string unique by appending a numerical suffix.
     *
     * @param elementContainer The container the new element is going to be
     * inserted to
     * @param elementName The name of the element
     */
    public XTextComponent insertTextFrame(int posX, int posY, int width, String text) throws Exception {

        // Create a unique name
        String uniqueName = createUniqueName(xDialogModelNameContainer, "TextFrame");

        // Create an editable text field control model
        Object oTextFrameModel = xMultiComponentFactoryDialogModel
                .createInstance("com.sun.star.text.TextFrame");

        // Set the properties at the model
        XPropertySet xTextFramePropertySet = (XPropertySet) UnoRuntime
                .queryInterface(XPropertySet.class, oTextFrameModel);
        xTextFramePropertySet.setPropertyValue("PositionX", new Integer(posX));
        xTextFramePropertySet.setPropertyValue("PositionY", new Integer(posY));
        xTextFramePropertySet.setPropertyValue("Height", new Integer(12));
        xTextFramePropertySet.setPropertyValue("Width", new Integer(width));
        xTextFramePropertySet.setPropertyValue("Text", (text != null) ? text : "");
        xTextFramePropertySet.setPropertyValue("Name", uniqueName);

        // Add the model to the dialog model name container
        xDialogModelNameContainer.insertByName(uniqueName, xTextFramePropertySet);

        // Reference the control by the unique name
        XControl xEditableTextFieldControl = xDialogContainer.getControl(uniqueName);

        return (XTextComponent) UnoRuntime.queryInterface(XTextComponent.class, xEditableTextFieldControl);
    }

    protected static String createUniqueName(XNameAccess elementContainer, String elementName) {

        String uniqueElementName = elementName;

        boolean elementExists = true;
        int i = 1;
        while (elementExists) {
            elementExists = elementContainer.hasByName(uniqueElementName);
            if (elementExists) {
                i++;
                uniqueElementName = elementName + Integer.toString(i);
            }
        }

        return uniqueElementName;
    }

    public XComboBox insertComboBox(int posX, int posY, int width, String[] stringItemList, String Name, String Text) {
        XComboBox xComboBox = null;
        try {
            // create a unique name by means of an own implementation...
            String sName = Name;//createUniqueName(xDialogModelNameContainer, "ComboBox");

            // create a controlmodel at the multiservicefactory of the dialog model... 
            Object oComboBoxModel = xMultiComponentFactoryDialogModel
                    .createInstance("com.sun.star.awt.UnoControlComboBoxModel");

            XPropertySet xComboBOxPropertySet = (XPropertySet) UnoRuntime
                    .queryInterface(XPropertySet.class, oComboBoxModel);

            /*
             //######################Debug de propriedades#########################
             XPropertySetInfo xCursorPropsInfo = xComboBOxPropertySet.getPropertySetInfo();
             Property[] aProps = xCursorPropsInfo.getProperties();
             int i;
             for (i = 0; i < aProps.length; ++i) {
             // prints Property Name
             System.out.println(aProps[i].Name);
             }
             */

            xComboBOxPropertySet.setPropertyValue("Dropdown", true);
            xComboBOxPropertySet.setPropertyValue("Step", 3);
            xComboBOxPropertySet.setPropertyValue("PositionX", new Integer(posX));
            xComboBOxPropertySet.setPropertyValue("PositionY", new Integer(posY));
            xComboBOxPropertySet.setPropertyValue("Height", new Integer(12));
            xComboBOxPropertySet.setPropertyValue("Width", new Integer(width));
            xComboBOxPropertySet.setPropertyValue("StringItemList", stringItemList);
            xComboBOxPropertySet.setPropertyValue("Name", sName);
            xComboBOxPropertySet.setPropertyValue("Text", Text);

            xDialogModelNameContainer.insertByName(sName, oComboBoxModel);
            XControl xControl = xDialogContainer.getControl(sName);

            // retrieve a ComboBox that is more convenient to work with than the Model of the ComboBox...
            xComboBox = (XComboBox) UnoRuntime.queryInterface(XComboBox.class, xControl);

        } catch (com.sun.star.uno.Exception ex) {
            SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
            SpeechOO.logger.error(ex);
        }
        return xComboBox;
    }

    public void showInfoBoxMessage(String Title, String Message, String type) {//type can be: infobox, 
        XComponent xComponent = null;
        try {
            Object oToolkit = xMultiComponentFactory.createInstanceWithContext("com.sun.star.awt.Toolkit", xComponentContext);
            XWindowPeer xParentWindowPeer = ((XToolkit) UnoRuntime.queryInterface(XToolkit.class, oToolkit)).getDesktopWindow();
            XMessageBoxFactory xMessageBoxFactory = (XMessageBoxFactory) UnoRuntime.queryInterface(XMessageBoxFactory.class, oToolkit);
            // rectangle may be empty if position is in the center of the parent peer    
            Rectangle aRectangle = new Rectangle();
            XMessageBox xMessageBox = xMessageBoxFactory.createMessageBox(xParentWindowPeer,
                    aRectangle, type, com.sun.star.awt.MessageBoxButtons.BUTTONS_OK, Title, Message);
            xComponent = (XComponent) UnoRuntime.queryInterface(XComponent.class, xMessageBox);
            if (xMessageBox != null) {
                short nResult = xMessageBox.execute();
            }
        } catch (Exception ex) {
            SpeechOO.logger = org.apache.log4j.Logger.getLogger(TrainingDialog.class.getName());
            SpeechOO.logger.error(ex);
        } finally {
            //make sure always to dispose the component and free the memory!
            if (xComponent != null) {
                xComponent.dispose();
            }
        }
    }
}