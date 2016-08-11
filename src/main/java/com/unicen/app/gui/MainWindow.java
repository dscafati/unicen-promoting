package com.unicen.app.gui;

import com.google.gson.Gson;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.unicen.app.AHP;
import com.unicen.app.App;
import com.unicen.app.ahp.Decision;
import com.unicen.app.indicators.Factory;
import com.unicen.app.indicators.Indicator;
import com.unicen.app.indicators.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dscafati on 7/15/16.
 */
public class MainWindow extends Component {
    private JPanel mainPanel;
    private JTabbedPane tabContainer;
    private JPanel indicatorsPanel;
    private JPanel mcdmPanel;
    private JList indicatorsList;
    private JTable indicatorsTable;
    private DefaultTableModel indicatorsTableModel;
    private JPanel mcdmIndicatorsPanel;
    private JLabel mcdmIndicatorsLabel;
    private JTable mcdmTable;
    private JButton mcdmShowChartButton;
    private JButton indicatorsShowChartButton;
    private JScrollPane indicatorsTableContainer;
    private JList checkBoxList;
    private JButton calculateButton;
    private JScrollPane mcdmTableContainer;
    private String indicatorsCurrentSelection;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setForeground(new Color(-6776680));
        tabContainer = new JTabbedPane();
        mainPanel.add(tabContainer, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        indicatorsPanel = new JPanel();
        indicatorsPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabContainer.addTab("Indicators", null, indicatorsPanel, "Indicators values");
        indicatorsList.setSelectionMode(0);
        indicatorsPanel.add(indicatorsList, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        indicatorsShowChartButton.setEnabled(false);
        indicatorsShowChartButton.setText("Show Chart");
        indicatorsPanel.add(indicatorsShowChartButton, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        indicatorsTableContainer = new JScrollPane();
        indicatorsPanel.add(indicatorsTableContainer, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        indicatorsTable.setAutoCreateRowSorter(false);
        indicatorsTable.setEnabled(false);
        indicatorsTable.setFillsViewportHeight(true);
        indicatorsTable.setShowHorizontalLines(true);
        indicatorsTable.setShowVerticalLines(false);
        indicatorsTableContainer.setViewportView(indicatorsTable);
        mcdmPanel = new JPanel();
        mcdmPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabContainer.addTab("MCDM", null, mcdmPanel, "AHP method results");
        mcdmIndicatorsPanel = new JPanel();
        mcdmIndicatorsPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mcdmPanel.add(mcdmIndicatorsPanel, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mcdmIndicatorsLabel = new JLabel();
        mcdmIndicatorsLabel.setText("Criteria");
        mcdmIndicatorsPanel.add(mcdmIndicatorsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mcdmIndicatorsPanel.add(checkBoxList, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        mcdmShowChartButton = new JButton();
        mcdmShowChartButton.setEnabled(false);
        mcdmShowChartButton.setText("Show Chart");
        mcdmPanel.add(mcdmShowChartButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        calculateButton.setText("Calculate");
        mcdmPanel.add(calculateButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mcdmTableContainer = new JScrollPane();
        mcdmPanel.add(mcdmTableContainer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mcdmTable.setEnabled(false);
        mcdmTable.setShowVerticalLines(false);
        mcdmTableContainer.setViewportView(mcdmTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    class DoubleComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Double d1 = (Double) o1;
            Double d2 = (Double) o2;
            return d1.compareTo(d2);
        }

        public boolean equals(Object o2) {
            return this.equals(o2);
        }
    }

    private void createUIComponents() {
        // Initialize right-side table
        indicatorsTable = new JTable();
        indicatorsTableModel = new DefaultTableModel();
        indicatorsTableModel.addColumn("School name");
        indicatorsTableModel.addColumn("Value");
        // Sorting as double
        TableRowSorter indicatorsTrs = new TableRowSorter(indicatorsTableModel);
        indicatorsTrs.setComparator(0, new DoubleComparator());
        indicatorsTable.setRowSorter(indicatorsTrs);

        indicatorsTable.setModel(indicatorsTableModel);

        // Enable or disable show chart button according to table data
        indicatorsTableModel.addTableModelListener(tableModelEvent -> {
            if (indicatorsTableModel.getRowCount() == 0) {
                indicatorsShowChartButton.setEnabled(false);
            } else {
                indicatorsShowChartButton.setEnabled(true);
            }
        });


        // Initialize left-side indicators list
        indicatorsList = new JList();
        DefaultListModel defaultListModel = new DefaultListModel();

        // Load indicators
        HashMap<String, String> indicators = Factory.listIndicators();
        String[] indicatorsKeysArray = indicators.keySet().toArray(new String[0]);

        // Add indicators to left-side list
        for (String i : indicators.keySet()) {
            defaultListModel.addElement(indicators.get(i));
        }
        indicatorsList.setModel(defaultListModel);

        // Action on left-side indicator selected
        indicatorsList.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                // Show waiting cursor
                mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                // Get index of the item selected on the list
                JList list = (JList) listSelectionEvent.getSource();
                int selections[] = list.getSelectedIndices();
                indicatorsCurrentSelection = indicatorsKeysArray[selections[0]];
                try {
                    // Execute the indicator associated function
                    List<Response> result = Factory.get(indicatorsCurrentSelection).evaluateAll();

                    // Show in the right-side table
                    // - Clear table data
                    while (indicatorsTableModel.getRowCount() > 0) {
                        indicatorsTableModel.removeRow(0);
                    }
                    // - Add new rows
                    for (Response r : result) {
                        indicatorsTableModel.addRow(r.asVector());
                    }

                } catch (Exception e) {
                    // Clear table data
                    while (indicatorsTableModel.getRowCount() > 0) {
                        indicatorsTableModel.removeRow(0);
                    }

                    // Display dialog with exception message
                    App.throwError(e);
                } finally {
                    // Restore default cursor
                    mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

            }

        });

        // Show chart button initialization
        indicatorsShowChartButton = new JButton();
        indicatorsShowChartButton.addActionListener(actionEvent -> {

            ArrayList<ArrayList<Object>> dataAsBeingShown = new ArrayList<ArrayList<Object>>();
            // Get sorted data
            for (int i = 0; i < Math.min(10, indicatorsTable.getRowCount()); i++) {
                // Get original index on the model
                Integer originalIndex = indicatorsTable.getRowSorter().convertRowIndexToModel(i);

                // Get each cell of this row
                ArrayList<Object> row = new ArrayList<Object>();
                for (int c = 0; c < indicatorsTableModel.getColumnCount(); c++) {
                    row.add(indicatorsTableModel.getValueAt(originalIndex, c));
                }
                dataAsBeingShown.add(row);
            }

            Gson gsonData = new Gson();

            String[] args = new String[]{
                    indicatorsCurrentSelection,
                    gsonData.toJson(dataAsBeingShown)
            };

            GraphWindow.main(args);
        });

        // Initialize checkBoxList
        checkBoxList = new CheckBoxList();
        DefaultListModel checkBoxModel = new DefaultListModel();


        // Load indicators
        HashMap<String, String> indicatorsCheck = Factory.listIndicators();


        // Add indicators
        for (String i : indicatorsCheck.keySet()) {
            JCheckBox box = new JCheckBox();
            box.setText(indicatorsCheck.get(i));
            checkBoxModel.addElement(box);
        }

        checkBoxList.setModel(checkBoxModel);

        // Calculate button initialization
        calculateButton = new JButton();
        calculateButton.addActionListener(actionEvent -> {
            CheckBoxList lista = (CheckBoxList) checkBoxList;
            if (lista.choosenAmount() > 0) {
                PairwiseWindow wnd = new PairwiseWindow(this, (CheckBoxList) checkBoxList);
                wnd.main();
            } else {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "You must select at least one element");
            }
        });

        // Initialize mcdm Table
        mcdmTable = new JTable();
        DefaultTableModel mcdmTableModel = new DefaultTableModel();
        mcdmTableModel.addColumn("School Name");
        mcdmTableModel.addColumn("Probability");
        mcdmTable.setModel(mcdmTableModel);

        // Enable or disable show chart button according to table data
        mcdmTableModel.addTableModelListener(tableModelEvent -> {
            if (mcdmTableModel.getRowCount() == 0) {
                mcdmShowChartButton.setEnabled(false);
            } else {
                mcdmShowChartButton.setEnabled(true);
            }
        });

    }

    public void showAHPResults(List<Indicator> indicators, double[][] indicatorsMatrix) {
        // Limpia la tabla
        DefaultTableModel model = (DefaultTableModel) mcdmTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++)
            model.removeRow(i);

        // Calcula AHP
        AHP ahp = new AHP(indicators, indicatorsMatrix);
        List<Decision> result = ahp.calculateDecision();

        // Muestra la tabla
        for (Decision d : result) {
            model.addRow(new Object[]{d.getSchoolName(), d.getProbability()});
        }

    }

}
