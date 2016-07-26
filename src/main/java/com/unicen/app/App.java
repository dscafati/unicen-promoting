package com.unicen.app;


import com.unicen.app.gui.MainWindow;
import com.unicen.app.indicators.Indicator;

import javax.swing.*;
import java.io.FileInputStream;


public class App
{
    private Indicator[] indicators;
    private double[][] indicatorsComparisonMatrix; 
    private static MainWindow mw;


    public static void main( String[] args )
    {
        try {
            // Load settings
            Config.loadProperties(new FileInputStream("settings.conf"));

            // Start main window
            mw.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            throwError(e);
        }
    }

    public static void throwError(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
