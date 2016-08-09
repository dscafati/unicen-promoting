package com.unicen.app.indicators;

import com.unicen.app.ahp.HigherCriterion;
import com.unicen.app.model.Facade;

import java.util.HashMap;
import java.util.List;

/**
 * This indicator measures the average calification of all students from each school
 */
public class AllAverageIndicator extends Indicator {

    public AllAverageIndicator() {
        this.setName("Average for all students");
        this.setCriterion(new HigherCriterion());
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getAverage();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error has occurred when trying to run \"getAllAverageIndicator()\" method from script");
        }
        return ret;
    }

    @Override
    public String getGraphType() {
        return Indicator.AS_BAR;
    }

    @Override
    public HashMap<String, Object> getExtraGraphData(){
        HashMap<String,Object> atts = new HashMap<>();
        atts.put("vertical_axis_label", "Averages");
        atts.put("horizontal_axis_label", "Schools");
        return atts;
    }
}