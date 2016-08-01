package com.unicen.app.gui;

import com.unicen.app.indicators.Factory;
import com.unicen.app.indicators.Indicator;
import sun.applet.Main;

import javax.swing.*;
import java.util.*;

/**
 * Created by Alfonso on 30/7/16.
 */
public class PairwiseWindow {
    private JPanel pairwisePanel;
    private JButton confirmButton;
    private CheckBoxList checkBoxList;
    private MainWindow mainWindow;

    public void main (MainWindow mw, CheckBoxList checkBoxList) {
        JFrame frame = new JFrame("MainWindow");
        frame.setMinimumSize(new java.awt.Dimension(300,300));
        frame.setContentPane(pairwisePanel);
        frame.pack();
        frame.setVisible(true);
        this.checkBoxList = checkBoxList;
        this.mainWindow=mw;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        confirmButton = new JButton();

        confirmButton.addActionListener(actionEvent -> {

            //chequear la matriz

            /*Si está correcta*/

            List<String> selected = checkBoxList.choosenValues();
            List<Indicator> indicators = new ArrayList<Indicator>();
            for (String indicator : selected) {
                indicators.add(Factory.get(indicator));
            }
            int n = indicators.size();
            double [][] indicatorsMatrix = new double [n][n];


            //copiar los valores que ingreso el usuario a la matriz

            //Prueba
            for (int i=0; i<n;i++)
                for (int j=0; j<n; j++)
                    indicatorsMatrix[i][j]=1;

            mainWindow.showAHPResults(indicators, indicatorsMatrix);

        });

    }


}
