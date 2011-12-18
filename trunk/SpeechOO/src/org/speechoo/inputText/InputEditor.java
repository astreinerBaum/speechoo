/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.speechoo.inputText;

import com.sun.star.awt.FontUnderline;
import com.sun.star.awt.FontWeight;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.text.XTextCursor;
import com.sun.star.uno.UnoRuntime;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 10080000701
 */
public class InputEditor {

   public static void setBold(XTextCursor xCursor) {
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharWeight", new Float(FontWeight.BOLD));
            
        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void BackBold(XTextCursor xCursor) {
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharWeight", new Float(FontWeight.NORMAL));

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void BackItalic(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharPosture", com.sun.star.awt.FontSlant.ITALIC);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void setItalic(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharPosture", com.sun.star.awt.FontSlant.ITALIC);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void BackUnderline(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharUnderline", FontUnderline.NONE);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void setUnderline(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharUnderline", FontUnderline.SINGLE);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setFontSize(XTextCursor xCursor, float size){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("CharHeight", new Float (size));

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);

        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputSentence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setParaPosCenter(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("ParaAdjust", ParagraphAdjust.CENTER);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setParaPosRight(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("ParaAdjust", ParagraphAdjust.RIGHT);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setParaPosLeft(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("ParaAdjust", ParagraphAdjust.LEFT);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setParaPosStretch(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("ParaAdjust", ParagraphAdjust.STRETCH);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setParaPosBlock(XTextCursor xCursor){
        XPropertySet xCursorProps = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, xCursor);
        try {

            xCursorProps.setPropertyValue("ParaAdjust", ParagraphAdjust.BLOCK);

        } catch (UnknownPropertyException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (com.sun.star.beans.PropertyVetoException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(InputEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
