package com.unicen.app.indicators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Indicator {
    private String name;

    public static final String AS_PIE = "pie";
    public static final String AS_BAR = "bar";

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public abstract List<Response> evaluateAll() throws Exception;

    public abstract String getGraphType();

    public HashMap<String, Object> getExtraGraphData(){ return new HashMap<String, Object>(); }
}
