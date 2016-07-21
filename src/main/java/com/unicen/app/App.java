package com.unicen.app;


import com.unicen.app.gui.MainWindow;
import com.unicen.app.indicators.IndicatorAbstract;

import javax.swing.*;


public class App
{
    private IndicatorAbstract[] indicators;
    private static MainWindow mw;

    public static void main( String[] args )
    {
        try {
            mw.main(args);
        } catch (Exception e) {
            throwError(e.getMessage());
        }
    }

    public static void throwError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
