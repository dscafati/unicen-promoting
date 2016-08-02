package com.unicen.app.model;

import com.unicen.app.Config;
import com.unicen.app.indicators.Response;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.sql.Sql;

import java.io.File;
import java.io.IOException;

import java.util.List;

public class Facade {
    private Script script;
    private SimpleDiskCache cache;

  //  private CacheAccess<String, List<Response>> cache = null;

    /**
     * Constructor
     * <p>
     * Initializes DB and load Script File
     */
    private Facade() throws IOException {
        // Initialize Cache
        this.cache = SimpleDiskCache.open(new File("cache"), 1,262144);
        // Intialize DBs
        Sql mysqlDB = null, pgsqlDB = null, informixDB = null;
        // Mysql
        String user = Config.getProperty("mysql_user");
        String host = Config.getProperty("mysql_host");
        String password = Config.getProperty("mysql_password");
        String db = Config.getProperty("mysql_db");
        try {
            mysqlDB = Sql.newInstance("jdbc:mysql://" + host + "/" + db + "?serverTimezone=UTC", user, password, "com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
        }

        // PostgreSql
        user = Config.getProperty("pgsql_user");
        host = Config.getProperty("pgsql_host");
        password = Config.getProperty("pgsql_password");
        db = Config.getProperty("pgsql_db");
        try {
            pgsqlDB = Sql.newInstance("jdbc:postgresql://" + host + "/" + db + "?serverTimezone=UTC", user, password, "org.postgresql.Driver");
        } catch (Exception e) {
        }


        // Informix
        user = Config.getProperty("informix_user");
        host = Config.getProperty("informix_host");
        password = Config.getProperty("informix_password");
        db = Config.getProperty("informix_db");
        try {
            informixDB = Sql.newInstance("jdbc:informix-sqli://" + host + "/" + db + "?serverTimezone=UTC", user, password, "com.informix.jdbc.IfxDriver");
        } catch (Exception e) {
        }


        Binding binding = new Binding();
        binding.setVariable("mysqlDB", mysqlDB);
        binding.setVariable("pgsqlDB", pgsqlDB);
        binding.setVariable("informixDB", informixDB);

        GroovyShell shell = new GroovyShell(binding);

        this.script = shell.parse(new File("model_scripts/script.groovy"));
    }


    // Singleton
    private static Facade instance = null;

    public static Facade getInstance() throws IOException {
        if (instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    public List<Response> getAverage() throws IOException{
        if (this.cache.contains("average")) {
            return this.cache.getResponse("average");
        } else {
            List<Response> result = (List<Response>) script.invokeMethod("getAllAverageIndicator", null);
            this.cache.putResponse("average", result);
            return result;
        }
    }

    public List<Response> getProgress() throws IOException{
        if (this.cache.contains("progress")) {
            return this.cache.getResponse("progress");
        } else {
            List<Response> result = (List<Response>) script.invokeMethod("getAverageDegreeOfProgress", null);
            this.cache.putResponse("progress", result);
            return result;
        }
    }

}
