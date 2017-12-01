/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Class;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 *
 * @author Achi
 */
public class Validation {
   private Border getborder;

    public boolean ValidateForm(JFrame frame) {
        boolean b = false;
        ArrayList<JTextField> jTextFields = new ArrayList();
        ArrayList<JPasswordField> jPasswordFields = new ArrayList();
        ArrayList<JLabel> jLabels = new ArrayList();
        ArrayList<JCheckBox> jCheckBoxs = new ArrayList();

        try {
            Container contentPane = frame.getContentPane();
            for (Component c : contentPane.getComponents()) {
                if (c instanceof JPanel) {
                    JPanel panel = (JPanel) c;
                    Component[] components = panel.getComponents();
                    for (Component component : components) {
                        if (component instanceof JTextField) {
                            jTextFields.add((JTextField) component);
                        } else if (component instanceof JPasswordField) {
                            jPasswordFields.add((JPasswordField) component);
                        } else if (component instanceof JLabel) {
                            jLabels.add((JLabel) component);
                        } else if (component instanceof JCheckBox) {
                            jCheckBoxs.add((JCheckBox) component);
                        }
                    }

                }
            }

            for (JTextField field : jTextFields) {
                field.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                        JTextField field = (JTextField) e.getSource();
                        field.setBorder(getborder);
                    }
                });
                if (field.getText().isEmpty() || field.getText().length() < 3) {
                    System.out.println("Empty");
                    Border border = BorderFactory.createLineBorder(Color.RED, 2);
                    field.setBorder(border);
                } else {
                    b = true;
                }
            }
            for (JPasswordField jpf : jPasswordFields) {
                jpf.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
                        JTextField field = (JTextField) e.getSource();
                        field.setBorder(getborder);

                    }
                });
                if (jpf.getText().isEmpty()) {
                    Border border = BorderFactory.createLineBorder(Color.RED, 2);
                    jpf.setBorder(border);
                }else{
                    b = true;
                }
                
            }

            return b;
        } catch (Exception e) {
            return false;
        }
    }
}
