package com.unicen.app.indicators;


import com.unicen.app.ahp.LowerCriterion;
import com.unicen.app.model.Facade;

import java.util.HashMap;
import java.util.List;

/**
 * This indicator measures the time it takes in average to finish the degree for all the students from each school
 */
public class DurationIndicator extends Indicator {

    public DurationIndicator() {
        this.setName("Major Duration");
        this.setCriterion(new LowerCriterion());
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getDuration();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error has occurred when trying to run \"getAverageDegreeDurationIndicator()\" method from script");
        }
        return ret;
    }

    @Override
    public String getGraphType() {
        return Indicator.AS_BAR;
    }

    @Override
    public HashMap<String, Object> getExtraGraphData() {
        HashMap<String, Object> atts = new HashMap<>();
        atts.put("vertical_axis_label", "Degree duration average");
        atts.put("horizontal_axis_label", "Schools");
        return atts;
    }
}