package com.unicen.app.gui;

import javax.swing.*;

/**
 * Created by Alfonso on 30/7/16.
 */
public class PairwiseWindow {
    private JPanel pairwisePanel;
    private JButton confirmButton;

    public void main (CheckBoxList checkBoxList) {
        JFrame frame = new JFrame("MainWindow");
        frame.setMinimumSize(new java.awt.Dimension(300,300));
        frame.setContentPane(pairwisePanel);
        frame.pack();
        frame.setVisible(true);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        confirmButton = new JButton();
    }
}
