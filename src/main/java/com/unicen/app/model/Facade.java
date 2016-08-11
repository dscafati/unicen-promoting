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
    private Boolean initializedScriptFlag = false;

    /**
     * Constructor
     *
     * Initializes DB and load Script File
     */
    private Facade() throws Exception {
        try {
            _initializeCache();
        }catch (IOException e){
            throw new Exception("Error when trying to initialize cache, check permissions");
        }
    }

    // Singleton
    private static Facade instance = null;

    public void refresh() throws IOException{
        this.cache.clear();
    }

    public static Facade getInstance() throws Exception {
        if (instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    private void _initializeCache() throws IOException {
        this.cache = SimpleDiskCache.open(new File("cache"), 1,262144);
    }
    private void _initializeScripts() throws IOException {
        if(initializedScriptFlag){
            return;
        }

        initializedScriptFlag = true;

        // Intialize DBs
        Sql mysqlDB = null, pgsqlDB = null, informixDB = null;

        // - Mysql
        String user = Config.getProperty("mysql_user");
        String host = Config.getProperty("mysql_host");
        String password = Config.getProperty("mysql_password");
        String db = Config.getProperty("mysql_db");
        try {
            mysqlDB = Sql.newInstance("jdbc:mysql://" + host + "/" + db + "?serverTimezone=UTC", user, password, "com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
        }

        // - PostgreSql
        user = Config.getProperty("pgsql_user");
        host = Config.getProperty("pgsql_host");
        password = Config.getProperty("pgsql_password");
        db = Config.getProperty("pgsql_db");
        try {
            pgsqlDB = Sql.newInstance("jdbc:postgresql://" + host + "/" + db + "?serverTimezone=UTC", user, password, "org.postgresql.Driver");
        } catch (Exception e) {
        }


        // - Informix
        user = Config.getProperty("informix_user");
        host = Config.getProperty("informix_host");
        password = Config.getProperty("informix_password");
        db = Config.getProperty("informix_db");
        try {
            informixDB = Sql.newInstance("jdbc:informix-sqli://" + host + "/" + db + "?serverTimezone=UTC", user, password, "com.informix.jdbc.IfxDriver");
        } catch (Exception e) {
        }


        // Groovy Script
        Binding binding = new Binding();
        binding.setVariable("mysqlDB", mysqlDB);
        binding.setVariable("pgsqlDB", pgsqlDB);
        binding.setVariable("informixDB", informixDB);

        GroovyShell shell = new GroovyShell(binding);

        this.script = shell.parse(new File("model_scripts/script.groovy"));
    }

    public List<Response> getAverage() throws IOException{
        if (this.cache.contains("average")) {
            return this.cache.getResponse("average");
        } else {
            _initializeScripts();
            List<Response> result = (List<Response>) script.invokeMethod("getAllAverageIndicator", null);
            this.cache.putResponse("average", result);
            return result;
        }
    }

    public List<Response> getProgress() throws IOException{
        if (this.cache.contains("progress")) {
            return this.cache.getResponse("progress");
        } else {
            _initializeScripts();
            List<Response> result = (List<Response>) script.invokeMethod("getAverageDegreeOfProgressIndicator", null);
            this.cache.putResponse("progress", result);
            return result;
        }
    }

    public List<Response> getDuration() throws IOException {
        if (this.cache.contains("duration")) {
            return this.cache.getResponse("duration");
        } else {
            _initializeScripts();
            List<Response> result = (List<Response>) script.invokeMethod("getAverageDegreeDurationIndicator", null);
            this.cache.putResponse("duration", result);
            return result;
        }
    }

    public List<Response> getGraduatedAverage() throws IOException {
        if (this.cache.contains("average-graduated")) {
            return this.cache.getResponse("average-graduated");
        } else {
            _initializeScripts();
            List<Response> result = (List<Response>) script.invokeMethod("getGraduatedAverageIndicator", null);
            this.cache.putResponse("average-graduated", result);
            return result;
        }
    }

    public List<Response> getGraduatedAverageAlt() throws IOException {
        if (this.cache.contains("average-graduated-alt")) {
            return this.cache.getResponse("average-graduated-alt");
        } else {
            _initializeScripts();
            List<Response> result = (List<Response>) script.invokeMethod("getGraduatedAverageAltIndicator", null);
            this.cache.putResponse("average-graduated-alt", result);
            return result;
        }
    }
    public List<Response> getDesertion() throws IOException {
        if (this.cache.contains("desertion")) {
            return this.cache.getResponse("desertion");
        } else {
            _initializeScripts();
            List<Response> result = (List<Response>) script.invokeMethod("getDesertionDegreeIndicator", null);
            this.cache.putResponse("desertion", result);
            return result;
        }
    }

}
