/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.speechoo.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 10080000501
 */
public class TableGramatical extends JPanel {
public static JFrame FrameGramatical = new JFrame("SimpleTableColors");
public Component CellEditor;
public static int cellSeparator;
public static String[] columnNameSugestions = {"Número","Sugestão",};

public static DefaultTableModel modelGramatical = new DefaultTableModel(columnNameSugestions, cellSeparator){
        @Override
     public boolean isCellEditable(int rowIndex, int mColIndex){
          return false;
     }
 };

public static JTable TableGramatical = new JTable(modelGramatical);
public TableGramatical(){
       super(new GridLayout(1,0));
       // TableGramatical.setPreferredScrollableViewportSize(new Dimension(500, 700));
        TableGramatical.setPreferredSize(new Dimension(400 ,TableGramatical.getRowCount()*16));
        TableGramatical.setFillsViewportHeight(true);
        setLayout(new BorderLayout());
        add(TableGramatical.getTableHeader(), BorderLayout.PAGE_START);
        add(TableGramatical, BorderLayout.CENTER);
}

public static void createAndShowGUI() {
        FrameGramatical.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TableGramatical newContentPane = new TableGramatical();
        newContentPane.setOpaque(true);
        FrameGramatical.setContentPane(newContentPane);
        FrameGramatical.setUndecorated(true);
        FrameGramatical.setLocationRelativeTo(null);
        FrameGramatical.pack();
        FrameGramatical.setVisible(true);
    }

public static void Gramatical() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
