package com.unicen.app.indicators;

import java.util.List;

public abstract class Indicator {
    private String name;

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public abstract List<Response> evaluateAll() throws Exception;
}
