package com.unicen.app.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.unicen.app.indicators.Factory;
import com.unicen.app.indicators.Indicator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.*;
import org.jfree.data.xy.DefaultHighLowDataset;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 * Created by dscafati on 7/15/16.
 */
public class GraphWindow {
    private ChartPanel chartPanel;
    private JPanel graphPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu aboutMenuItem;
    private JMenuItem exportMenuItem;
    private JMenuItem exitMenuItem;

    private static Object[][] data;
    private static String indicator;

    public static void main(String[] args) {
        Gson data = new Gson();
        GraphWindow.indicator = args[0];
        GraphWindow.data = data.fromJson(args[1], Object[][].class);

        JFrame frame = new JFrame("GraphWindow");
        frame.setContentPane(new GraphWindow().graphPanel);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private JFreeChart _drawAsPie() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Object[] row : GraphWindow.data) {
            dataset.setValue((String) row[0], ((BigDecimal) row[1]).doubleValue());
        }

        return ChartFactory.createPieChart3D(
                Factory.get(GraphWindow.indicator).getName(),
                dataset,
                true,
                true,
                false
        );
    }

    private JFreeChart _drawAsBar() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Object[] row : GraphWindow.data) {
            dataset.addValue(((BigDecimal) row[1]).doubleValue(), (String) row[0], "");
        }
        HashMap<String, Object> atts = Factory.get(GraphWindow.indicator).getExtraGraphData();

        return ChartFactory.createBarChart3D(
                Factory.get(GraphWindow.indicator).getName(),
                (String) atts.get("horizontal_axis_label"),
                (String) atts.get("vertical_axis_label"),
                dataset,
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );
    }

    private void createUIComponents() {
        switch (Factory.get(GraphWindow.indicator).getGraphType()) {
            case Indicator.AS_PIE:
                chartPanel = new ChartPanel(_drawAsPie());
                break;
            case Indicator.AS_BAR:
            default:
                chartPanel = new ChartPanel(_drawAsBar());
                break;
        }
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
        graphPanel = new JPanel();
        graphPanel.setLayout(new BorderLayout(0, 0));
        menuBar = new JMenuBar();
        menuBar.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        graphPanel.add(menuBar, BorderLayout.NORTH);
        fileMenu = new JMenu();
        fileMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        fileMenu.setText("File");
        fileMenu.setMnemonic('F');
        fileMenu.setDisplayedMnemonicIndex(0);
        menuBar.add(fileMenu, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        exportMenuItem = new JMenuItem();
        exportMenuItem.setText("Export Image");
        exportMenuItem.setMnemonic('E');
        exportMenuItem.setDisplayedMnemonicIndex(0);
        fileMenu.add(exportMenuItem);
        final JSeparator separator1 = new JSeparator();
        fileMenu.add(separator1);
        exitMenuItem = new JMenuItem();
        exitMenuItem.setText("Exit");
        exitMenuItem.setMnemonic('E');
        exitMenuItem.setDisplayedMnemonicIndex(0);
        fileMenu.add(exitMenuItem);
        final Spacer spacer1 = new Spacer();
        menuBar.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        aboutMenuItem = new JMenu();
        aboutMenuItem.setText("About");
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setDisplayedMnemonicIndex(0);
        menuBar.add(aboutMenuItem, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        chartPanel.setDomainZoomable(true);
        graphPanel.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return graphPanel;
    }
}
