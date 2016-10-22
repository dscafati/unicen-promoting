package com.unicen.app.gui;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.unicen.app.AHP;
import com.unicen.app.App;
import com.unicen.app.ahp.Decision;
import com.unicen.app.indicators.Factory;
import com.unicen.app.indicators.Indicator;
import com.unicen.app.indicators.Response;
import com.unicen.app.model.Facade;
import com.unicen.app.model.SimpleDiskCache;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
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
    private JTable mcdmTable;
    private JButton mcdmShowChartButton;
    private JButton indicatorsShowChartButton;
    private JScrollPane indicatorsTableContainer;
    private JList checkBoxList;
    private JButton calculateButton;
    private JScrollPane mcdmTableContainer;
    private JMenu helpMenu;
    private JMenu fileMenu;
    private JMenuItem refreshDataMenuItem;
    private JButton indicatorsExportButton;
    private JButton mcdmExportButton;
    private JMenuItem aboutMenuItem;
    private String indicatorsCurrentSelection;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Unicen Promoting Tool");
        frame.setContentPane(new MainWindow().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void createUIComponents() {
        // Initialize right-side table
        indicatorsTable = new JTable();
        indicatorsTableModel = new DefaultTableModel(new Object[0][], new String[]{
                "School name", "Value"}) {
            Class[] types = {String.class, Double.class};
            boolean[] canEdit = {false, false};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int columnIndex) {
                return this.canEdit[columnIndex];
            }
        };


        indicatorsTable.setModel(indicatorsTableModel);

        // Enable or disable show chart button according to table data
        indicatorsTableModel.addTableModelListener(tableModelEvent -> {
            if (indicatorsTableModel.getRowCount() == 0) {
                indicatorsShowChartButton.setEnabled(false);
                indicatorsExportButton.setEnabled(false);
            } else {
                indicatorsShowChartButton.setEnabled(true);
                indicatorsExportButton.setEnabled(true);
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
            this._showChart();
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
            // Limpia la tabla
            DefaultTableModel model = (DefaultTableModel) mcdmTable.getModel();
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
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
        DefaultTableModel mcdmTableModel = new DefaultTableModel(new Object[0][], new String[]{
                "School name", "Value (%)"}) {
            Class[] types = {String.class, Double.class};
            boolean[] canEdit = {false, false};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int columnIndex) {
                return this.canEdit[columnIndex];
            }
        };


        mcdmTable.setModel(mcdmTableModel);

        // Enable or disable show chart / Export buttons according to table data
        mcdmTableModel.addTableModelListener(tableModelEvent -> {
            if (mcdmTableModel.getRowCount() == 0) {
                mcdmShowChartButton.setEnabled(false);
                mcdmExportButton.setEnabled(false);
            } else {
                mcdmShowChartButton.setEnabled(true);
                mcdmExportButton.setEnabled(true);
            }
        });
        

        // Menu actions
        aboutMenuItem = new JMenuItem();
        aboutMenuItem.addActionListener(actionEvent -> {
            AboutWindow.main(null);
        });
        // - Clear Cache
        refreshDataMenuItem = new JMenuItem();
        refreshDataMenuItem.addActionListener(actionEvent -> {
            try {
                Facade.getInstance().refresh();
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Cache cleared successfully.");
            } catch (Exception e) {
                App.throwError(e);
            }

        });
        // - Export
        final JFileChooser fc = new JFileChooser();
        indicatorsExportButton = new JButton();
        indicatorsExportButton.addActionListener(actionEvent -> {

            int returnVal = fc.showSaveDialog(this);

            class DataType {
                public String name;
                public Double value;
            }

            ArrayList<DataType> export = new ArrayList<DataType>();

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File outputFile = fc.getSelectedFile();

                for (int i = 0; i < indicatorsTable.getRowCount(); i++) {
                    Integer sortIndex = indicatorsTable.getRowSorter().convertRowIndexToModel(i);

                    DataType row = new DataType();
                    row.name = (String) indicatorsTableModel.getValueAt(sortIndex, 0);
                    row.value = (Double) indicatorsTableModel.getValueAt(sortIndex, 1);
                    export.add(row);

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


    }

    private void _showChart() {


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
    }


    public void showAHPResults(List<Indicator> indicators, double[][] indicatorsMatrix) {
        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // Calcula AHP
        AHP ahp = new AHP(indicators, indicatorsMatrix);
        List<Decision> result = ahp.calculateDecision();

        // Muestra la tabla
        DefaultTableModel model = (DefaultTableModel) mcdmTable.getModel();
        for (Decision d : result) {
            model.addRow(new Object[]{d.getSchoolName(), d.getProbability()});
        }
        mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


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
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setForeground(new Color(-6776680));
        tabContainer = new JTabbedPane();
        mainPanel.add(tabContainer, BorderLayout.CENTER);
        indicatorsPanel = new JPanel();
        indicatorsPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabContainer.addTab("Indicators", null, indicatorsPanel, "Indicators values");
        indicatorsList.setSelectionMode(0);
        indicatorsPanel.add(indicatorsList, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        indicatorsTableContainer = new JScrollPane();
        indicatorsPanel.add(indicatorsTableContainer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        indicatorsTable.setAutoCreateRowSorter(true);
        indicatorsTable.setEnabled(false);
        indicatorsTable.setFillsViewportHeight(true);
        indicatorsTable.setShowHorizontalLines(true);
        indicatorsTable.setShowVerticalLines(false);
        indicatorsTableContainer.setViewportView(indicatorsTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        indicatorsPanel.add(panel1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        indicatorsShowChartButton.setEnabled(false);
        indicatorsShowChartButton.setText("Show Chart");
        indicatorsShowChartButton.setMnemonic('S');
        indicatorsShowChartButton.setDisplayedMnemonicIndex(0);
        panel1.add(indicatorsShowChartButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        indicatorsExportButton.setEnabled(false);
        indicatorsExportButton.setText("Export");
        indicatorsExportButton.setMnemonic('E');
        indicatorsExportButton.setDisplayedMnemonicIndex(0);
        panel1.add(indicatorsExportButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mcdmPanel = new JPanel();
        mcdmPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabContainer.addTab("MCDM", null, mcdmPanel, "AHP method results");
        mcdmIndicatorsPanel = new JPanel();
        mcdmIndicatorsPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mcdmPanel.add(mcdmIndicatorsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mcdmIndicatorsLabel = new JLabel();
        mcdmIndicatorsLabel.setText("Criteria");
        mcdmIndicatorsPanel.add(mcdmIndicatorsLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mcdmIndicatorsPanel.add(checkBoxList, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        calculateButton.setText("Calculate");
        mcdmPanel.add(calculateButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mcdmTableContainer = new JScrollPane();
        mcdmPanel.add(mcdmTableContainer, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        mcdmTable.setAutoCreateRowSorter(false);
        mcdmTable.setEnabled(false);
        mcdmTable.setFillsViewportHeight(true);
        mcdmTable.setShowVerticalLines(false);
        mcdmTableContainer.setViewportView(mcdmTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mcdmPanel.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        mcdmShowChartButton = new JButton();
        mcdmShowChartButton.setEnabled(false);
        mcdmShowChartButton.setText("Show Chart");
        mcdmShowChartButton.setMnemonic('S');
        mcdmShowChartButton.setDisplayedMnemonicIndex(0);
        panel2.add(mcdmShowChartButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        mcdmExportButton = new JButton();
        mcdmExportButton.setEnabled(false);
        mcdmExportButton.setText("Export");
        mcdmExportButton.setMnemonic('E');
        mcdmExportButton.setDisplayedMnemonicIndex(0);
        panel2.add(mcdmExportButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JMenuBar menuBar1 = new JMenuBar();
        menuBar1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(menuBar1, BorderLayout.NORTH);
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
        final Spacer spacer3 = new Spacer();
        menuBar1.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        fileMenu = new JMenu();
        fileMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        fileMenu.setText("File");
        fileMenu.setMnemonic('F');
        fileMenu.setDisplayedMnemonicIndex(0);
        menuBar1.add(fileMenu, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        refreshDataMenuItem.setText("Refresh Data");
        refreshDataMenuItem.setMnemonic('R');
        refreshDataMenuItem.setDisplayedMnemonicIndex(0);
        refreshDataMenuItem.setToolTipText("Clears the cache");
        fileMenu.add(refreshDataMenuItem);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
