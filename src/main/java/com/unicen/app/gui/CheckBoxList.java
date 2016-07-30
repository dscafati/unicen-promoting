package com.unicen.app.gui;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by Alfonso on 30/7/16.
 */
public class CheckBoxList extends JList {
    protected static Border noFocusBorder =
            new EmptyBorder(1, 1, 1, 1);

    public CheckBoxList() {
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter() {
                             public void mousePressed(MouseEvent e) {
                                 int index = locationToIndex(e.getPoint());

                                 if (index != -1) {
                                     JCheckBox checkbox = (JCheckBox)
                                             getModel().getElementAt(index);
                                     checkbox.setSelected(
                                             !checkbox.isSelected());
                                     repaint();
                                 }
                             }
                         }
        );

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public java.util.List<String> choosenValues () {
        java.util.List<String> result = new ArrayList<String>();
        for (int i=0; i<getModel().getSize(); i++) {
            JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
            if (checkbox.isSelected())
                result.add(checkbox.getText());
        }
        return result;
    }

    public int choosenAmount () {
        return this.choosenValues().size();
    }

    protected class CellRenderer implements ListCellRenderer {
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected ?
                    getSelectionBackground() : getBackground());
            checkbox.setForeground(isSelected ?
                    getSelectionForeground() : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected ?
                    UIManager.getBorder(
                            "List.focusCellHighlightBorder") : noFocusBorder);
            return checkbox;
        }
    }
}
