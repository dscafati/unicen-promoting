package com.unicen.app.gui;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
//import com.sun.tools.javac.comp.Check;
import com.unicen.app.App;
import com.unicen.app.indicators.Factory;
import com.unicen.app.indicators.Indicator;

import javax.swing.*;


import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;

import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Alfonso on 30/7/16.
 */
public class PairwiseWindow {
    private JPanel pairwisePanel;
    private JButton confirmButton;
    private JTabbedPane tabbedPane1;
    private JTable editableMatrix;
    private JTextPane helpText;
    private JMenu FileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private CheckBoxList checkBoxList;
    private MainWindow mainWindow;
    private JFrame frame;

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        pairwisePanel = new JPanel();
        pairwisePanel.setLayout(new BorderLayout(0, 0));
        final JMenuBar menuBar1 = new JMenuBar();
        menuBar1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        pairwisePanel.add(menuBar1, BorderLayout.NORTH);
        FileMenu = new JMenu();
        FileMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        FileMenu.setText("File");
        FileMenu.setMnemonic('F');
        FileMenu.setDisplayedMnemonicIndex(0);
        menuBar1.add(FileMenu, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        openMenuItem.setText("Open");
        openMenuItem.setMnemonic('O');
        openMenuItem.setDisplayedMnemonicIndex(0);
        FileMenu.add(openMenuItem);
        saveMenuItem.setText("Save as...");
        saveMenuItem.setMnemonic('S');
        saveMenuItem.setDisplayedMnemonicIndex(0);
        FileMenu.add(saveMenuItem);
        final Spacer spacer1 = new Spacer();
        menuBar1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        helpMenu = new JMenu();
        helpMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        helpMenu.setText("Help");
        helpMenu.setMnemonic('H');
        helpMenu.setDisplayedMnemonicIndex(0);
        menuBar1.add(helpMenu, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        aboutMenuItem.setText("About");
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setDisplayedMnemonicIndex(0);
        helpMenu.add(aboutMenuItem);
        tabbedPane1 = new JTabbedPane();
        pairwisePanel.add(tabbedPane1, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Comparison Matrix", panel1);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        editableMatrix.setFillsViewportHeight(true);
        scrollPane1.setViewportView(editableMatrix);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Help", panel2);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        helpText = new JTextPane();
        helpText.setEditable(false);
        helpText.setText("Usage:\n*******\n\nThe comparison matrix allows to make paired comparisons between the choosen indicators.\n\n1- Choose a scale to make judgements. Preferrable: 1 to 9\n\n2- The value in Matrix[i][j] represents the judgement value between the indicator in the row \"i\" and the indicator in the row \"j\". This values must be filled up using the following rules:\n\n-if indicator \"i\" is better than indicator \"j\", put the actual judgement value.\n- if indicator \"j\" is better than indicator \"i\", put the reciprocal judgement value.\n\n3- It is only necesary to fill up the upper triangular matrix, since the diagonal elements are always 1 and the lower matrix is reciprocal to the upper diagonal.\n\n");
        scrollPane2.setViewportView(helpText);
        confirmButton.setEnabled(true);
        confirmButton.setText("Confirm");
        pairwisePanel.add(confirmButton, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return pairwisePanel;
    }

    class DataType {
        public String i1;
        public String i2;
        public Double value;

        public DataType(String i1, String i2, Double value) {
            this.i1 = i1;
            this.i2 = i2;
            this.value = value;
        }
    }


    public PairwiseWindow(MainWindow mw, CheckBoxList checkBoxList) {
        $$$setupUI$$$();
        this.checkBoxList = checkBoxList;
        this.mainWindow = mw;
    }

    public void main() {
        frame = new JFrame("Criteria Comparison");
        frame.setMinimumSize(new Dimension(300, 300));
        frame.setContentPane(pairwisePanel);
        frame.pack();
        frame.setVisible(true);
        helpText.setCaretPosition(0);
        List<String> selected = this.checkBoxList.choosenValues();
        int n = selected.size() + 1;
        MatrixModel tableModel = new MatrixModel(n, n);


        tableModel.setRowCount(n);
        tableModel.setColumnCount(n);

        for (int i = 1; i < n; i++) {
            tableModel.setValueAt(selected.get(i - 1), 0, i);
            tableModel.setValueAt(selected.get(i - 1), i, 0);
        }


        editableMatrix.setModel(tableModel);

        for (int i = 1; i < n; i++)
            for (int j = 1; j < n; j++) {
                if (i == j) {
                    tableModel.setValueAt(1, i, j);
                    tableModel.setCellEditable(i, j, false);
                } else if (i > j) {
                    tableModel.setCellEditable(i, j, false);
                } else {
                    tableModel.setCellEditable(i, j, true);

                }

            }

    }

    private void createUIComponents() {

        confirmButton = new JButton();
        editableMatrix = new JTable();
        editableMatrix.setTableHeader(new JTableHeader());


        confirmButton.addActionListener(actionEvent -> {
            this.frame.dispose();
            List<String> selected = this.checkBoxList.choosenValues();
            Integer N = selected.size();
            //chequear la matriz
            /*Si está correcta*/
            List<Indicator> indicators = new ArrayList<Indicator>();

            HashMap<String, String> map = Factory.listIndicators();
            for (String key : map.keySet()) {
                String indicator = map.get(key);
                if (selected.contains(indicator))
                    indicators.add(Factory.get(key));
            }

            double[][] indicatorsMatrix = new double[N][N];


            //copiar los valores que ingreso el usuario a la matriz

            //Prueba
            for (int i = 1; i <= N; i++)
                for (int j = 1; j <= N; j++) {
                    Double value = Double.parseDouble(editableMatrix.getModel().getValueAt(i, j).toString());
                    indicatorsMatrix[i - 1][j - 1] = value.doubleValue();
                }



            this.confirmButton.setEnabled(false);
            mainWindow.showAHPResults(indicators, indicatorsMatrix);


        });

        // Menu
        aboutMenuItem = new JMenuItem();
        aboutMenuItem.addActionListener(actionEvent -> {
            AboutWindow.main(null);
        });

        saveMenuItem = new JMenuItem();
        saveMenuItem.addActionListener(actionEvent -> {
            List<String> selected = this.checkBoxList.choosenValues();
            Integer N = selected.size();

            JFileChooser fc = new JFileChooser();
            int value = fc.showSaveDialog(this.pairwisePanel);
            if (value == JFileChooser.APPROVE_OPTION) {
                File outputFile = fc.getSelectedFile();

                ArrayList<DataType> export = new ArrayList<DataType>();

                // Export the upper-right matrix triangle
                for (int i = 2; i <= N; i++) {
                    for (int j = 1; j < i; j++) {
                        DataType row = new DataType(selected.get(i-1), selected.get(j-1), (Double)editableMatrix.getValueAt(i, j));
                        export.add(row);
                    }
                }

                // Export as CSV
                CsvMapper mapper = new CsvMapper();
                CsvSchema schema = mapper.schemaFor(DataType.class);
                schema = schema.withColumnSeparator('\t');

                // output writer
                ObjectWriter myObjectWriter = mapper.writer(schema);
                try {
                    FileOutputStream tempFileOutputStream = new FileOutputStream(outputFile);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
                    OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");
                    myObjectWriter.writeValue(writerOutputStream, export);
                } catch (Exception e) {
                    App.throwError(e);
                }
            }

        });

        openMenuItem = new JMenuItem();
        openMenuItem.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
            int value = fc.showOpenDialog(this.pairwisePanel);
            if (value == JFileChooser.APPROVE_OPTION) {
                File inputFile = fc.getSelectedFile();
                CsvMapper mapper = new CsvMapper();
                CsvSchema schema = mapper.schemaFor(DataType.class);
                schema = schema.withColumnSeparator('\t');
                try {
                    MappingIterator<DataType> it = mapper.readerFor(DataType.class).with(schema).readValues(inputFile);
                    while (it.hasNext()) {
                        DataType row = it.next();
                        System.out.println(row);

                    }
                } catch (Exception e) {
                    App.throwError(e);
                }
            }

        });


    }


}
