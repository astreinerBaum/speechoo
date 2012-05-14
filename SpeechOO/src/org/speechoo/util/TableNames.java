/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.util;

/**
 *
 * @author Welton Araújo
 */

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.table.DefaultTableModel;

public class TableNames extends JPanel {
    public static JFrame FrameNames = new JFrame("SimpleTableNames");
   
    public Component CellEditor;
    public static String[] columnFontNames = {"Número","Nome",};
    public static Object[][] dataFontNames = {
        {"1", "Times New Roman"},
        {"2", "Arial"},
        {"3", "Verdana"},
        {"4", "ComicSansMS"},
        {"5", "LucidaSans"}};
    

    public static DefaultTableModel ModelNames = new DefaultTableModel(dataFontNames, columnFontNames){
        @Override

     public boolean isCellEditable(int rowIndex, int mColIndex){
          return false;   
     }   
 };
public static JTable TableNames = new JTable(ModelNames);
public TableNames(){
        super(new GridLayout(1,0));
        //TableNames.
        TableNames.setPreferredSize(new Dimension(300,80));
        TableNames.setFillsViewportHeight(false);
        setLayout(new BorderLayout());
        add(TableNames.getTableHeader(), BorderLayout.PAGE_START);
        add(TableNames, BorderLayout.CENTER);
}


public static void createAndShowGUI() {
        FrameNames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TableNames newContentPane = new TableNames();
        newContentPane.setOpaque(true);
        FrameNames.setContentPane(newContentPane);
        FrameNames.setUndecorated(true);
        FrameNames.setLocationRelativeTo(null);
        FrameNames.pack();
        FrameNames.setVisible(true);
    }

public static void Names() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

