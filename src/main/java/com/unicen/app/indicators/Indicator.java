package com.unicen.app.indicators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Indicator {
    private String name;

    //@todo ver como setear el criterio. Si es por constructor o setter
    private Criterion criterion;

    public static final String AS_PIE = "pie";
    public static final String AS_BAR = "bar";

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }
    
    public boolean isBetter (double value1, double value2) {
    	return criterion.isBetter(value1, value2);
    }

    public double getMatrixComparisonValue(double value1, double value2) {
    	double matrixValue = 0;
    	
    	if (value1>value2) 
    		matrixValue = value1/value2;
    	else
    		matrixValue = value2/value1;
    	
    	if (this.isBetter(value1, value2))
    		return matrixValue;
    	else
    		return (1/matrixValue);
    	
    	
    }

    public abstract List<Response> evaluateAll() throws Exception;

    public abstract String getGraphType();

    public HashMap<String, Object> getExtraGraphData(){ return new HashMap<String, Object>(); }
}
