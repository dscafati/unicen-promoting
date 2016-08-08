package com.unicen.app.indicators;

import com.unicen.app.ahp.HigherCriterion;
import com.unicen.app.model.Facade;

import java.util.HashMap;
import java.util.List;

/**
 * This indicator measures the average calification of all the graduated students from each school
 */
public class GraduatedAverageIndicator extends Indicator {

    public GraduatedAverageIndicator() {
        this.setName("Averages for graduated students");
        this.setCriterion(new HigherCriterion());
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getGraduatedAverage();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error has occurred when trying to run \"getGraduatedAverageIndicator()\" method from script");
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