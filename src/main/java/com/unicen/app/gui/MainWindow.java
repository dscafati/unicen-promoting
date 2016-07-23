package com.unicen.app.gui;

import com.google.gson.Gson;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.unicen.app.App;
import com.unicen.app.indicators.Factory;
import com.unicen.app.indicators.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
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
    private JCheckBox checkBox1;
    private JTable mcdmTable;
    private JButton mcdmShowChartButton;
    private JButton indicatorsShowChartButton;
    private JScrollPane indicatorsTableContainer;
    private String indicatorsCurrentSelection;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // Initialize right-side table
        indicatorsTable = new JTable();
        indicatorsTableModel = new DefaultTableModel();
        indicatorsTableModel.addColumn("School name");
        indicatorsTableModel.addColumn("Value");
        indicatorsTable.setModel(indicatorsTableModel);

        // Enable or disable show chart button according to table data
        indicatorsTableModel.addTableModelListener(tableModelEvent -> {
            if( indicatorsTableModel.getRowCount() == 0 ){
                indicatorsShowChartButton.setEnabled(false);
            }else{
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
                indicatorsCurrentSelection = indicatorsKeysArray[listSelectionEvent.getFirstIndex()];
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
            for( int i = 0; i < Math.min(10, indicatorsTable.getRowCount()); i++) {
                // Get original index on the model
                Integer originalIndex = indicatorsTable.getRowSorter().convertRowIndexToModel(i);

                // Get each cell of this row
                ArrayList<Object> row = new ArrayList<Object>();
                for( int c =0; c < indicatorsTableModel.getColumnCount(); c++){
                    row.add(indicatorsTableModel.getValueAt(originalIndex,c));
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
        indicatorsTable.setAutoCreateRowSorter(true);
        indicatorsTable.setFillsViewportHeight(true);
        indicatorsTable.setShowHorizontalLines(true);
        indicatorsTable.setShowVerticalLines(true);
        indicatorsTableContainer.setViewportView(indicatorsTable);
        mcdmPanel = new JPanel();
        mcdmPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabContainer.addTab("MCDM", null, mcdmPanel, "AHP method results");
        mcdmIndicatorsPanel = new JPanel();
        mcdmIndicatorsPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mcdmPanel.add(mcdmIndicatorsPanel, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mcdmIndicatorsLabel = new JLabel();
        mcdmIndicatorsLabel.setText("Criteria");
        mcdmIndicatorsPanel.add(mcdmIndicatorsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mcdmIndicatorsPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        checkBox1 = new JCheckBox();
        checkBox1.setText("CheckBox");
        mcdmIndicatorsPanel.add(checkBox1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mcdmTable = new JTable();
        mcdmTable.setAutoCreateRowSorter(true);
        mcdmPanel.add(mcdmTable, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        mcdmShowChartButton = new JButton();
        mcdmShowChartButton.setText("Show Chart");
        mcdmPanel.add(mcdmShowChartButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
