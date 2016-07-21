package com.unicen.app;


import com.unicen.app.gui.MainWindow;
import com.unicen.app.indicators.IndicatorAbstract;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class App
{
    private IndicatorAbstract[] indicators;
    private static MainWindow mw;


    public static void main( String[] args )
    {
        try {
            // Load settings
            Config.loadProperties(new FileInputStream("settings.conf"));

            // Start main window
            mw.main(args);
        } catch (Exception e) {
            throwError(e.getMessage());
        }
    }

    public static void throwError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
