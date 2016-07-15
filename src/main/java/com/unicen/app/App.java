package com.unicen.app;

import com.informix.asf.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

import java.io.File;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.lang.GroovyShell;




public class App 
{
    public static void main( String[] args )
    {

        // Hello world UI
/*
        JLabel hwLabel = new JLabel("Hello World!");
        hwLabel.setHorizontalAlignment(JLabel.CENTER);
        hwLabel.setOpaque(true);


        JWindow mainWindow = new JWindow();
        mainWindow.add(hwLabel, BorderLayout.CENTER);
        mainWindow.setSize(400,400);
        hwLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        mainWindow.setVisible(true);
*/

        // Hello world model

		// lets pass in some variables
		Binding binding = new Binding();
		binding.setVariable("cheese", "Cheddar");
		binding.setVariable("args", args);
try {

    GroovyShell shell = new GroovyShell(binding);

    Script value = shell.parse(new File("model_scripts/model.scr"));
    value.invokeMethod("test",new Object());
}catch (Exception e){
    System.out.println("error");
    System.out.println(e.getMessage());
}




        // Hello world
        System.out.println( "Hello World!" );

    }
}
