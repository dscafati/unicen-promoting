package com.unicen.app.indicators;


import com.unicen.app.App;
import com.unicen.app.model.Facade;

import java.util.List;

public class AverageIndicator extends Indicator {

    public AverageIndicator() {
        this.setName("Average");
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getAverage();
        } catch (Exception e) {
            throw new Exception("An error has occurred when trying to run \"getAllAverageIndicator()\" method from script");
        }
        return ret;
    }
    // public List<Pair> results;

    //public List<Pair> evaluateAll(){

    //     return null;
    // }

}
