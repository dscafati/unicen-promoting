package com.unicen.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties p;

    public static void loadProperties(InputStream in) throws IOException {
        // Default settings
        Properties defaultProps = new Properties();
        defaultProps.put("mysql_host", "");
        defaultProps.put("mysql_user", "");
        defaultProps.put("mysql_password", "");
        defaultProps.put("mysql_db", "");
        defaultProps.put("pgsql_host", "");
        defaultProps.put("pgsql_user", "");
        defaultProps.put("pgsql_password", "");
        defaultProps.put("pgsql_db", "");
        defaultProps.put("informix_host", "");
        defaultProps.put("informix_user", "");
        defaultProps.put("informix_password", "");
        defaultProps.put("informix_db", "");

        // Load from file
        Properties applicationProps = new Properties(defaultProps);
        applicationProps.load(in);
        in.close();

        // Store
        Config.p = applicationProps;
    }
    public static String getProperty(String name) {
        return p.getProperty(name);
    }
}
