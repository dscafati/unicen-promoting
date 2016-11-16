package com.unicen.app.indicators;

import com.unicen.app.ahp.LowerCriterion;
import com.unicen.app.model.Facade;

import java.util.HashMap;
import java.util.List;

/**
 * This indicator measures the degree of desertion of students from each school
 */
public class DesertionIndicator extends Indicator {

    public DesertionIndicator() {
        this.setName("Desertion degree");
        this.setCriterion(new LowerCriterion());
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getDesertion();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error has occurred when trying to run \"getDesertionDegreeIndicator()\" method from script");
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