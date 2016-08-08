package com.unicen.app.indicators;

import com.unicen.app.ahp.HigherCriterion;
import com.unicen.app.model.Facade;

import java.util.HashMap;
import java.util.List;

/**
 * This indicator measures the average degree of advance in the degree, for the students of each school
 */
public class ProgressIndicator extends Indicator {

    public ProgressIndicator() {
        this.setName("Degree of progress");
        this.setCriterion(new HigherCriterion());
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getProgress();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error has occurred when trying to run \"getAverageDegreeOfProgressIndicator()\" method from script");
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
        atts.put("vertical_axis_label", "Progress average among students (# subjects approved)");
        atts.put("horizontal_axis_label", "Schools");
        return atts;
    }
}