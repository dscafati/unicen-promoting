package com.unicen.app;

import java.util.Properties;

public class Config {
    private static Properties p;

    public static void setProperties(Properties p) {
        Config.p = p;
    }

    public static String getProperty(String name) {
        return p.getProperty(name);
    }
}
