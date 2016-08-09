package com.unicen.app.indicators;

import com.unicen.app.ahp.HigherCriterion;
import com.unicen.app.model.Facade;

import java.util.HashMap;
import java.util.List;

/**
 * This indicator measures the average calification of all the graduated students from each school, only approved notes
 */
public class GraduatedAverageAltIndicator extends Indicator {

    public GraduatedAverageAltIndicator() {
        this.setName("Averages for graduated students without fails");
        this.setCriterion(new HigherCriterion());
    }

    public List<Response> evaluateAll() throws Exception {
        List<Response> ret;
        try {
            ret = Facade.getInstance().getGraduatedAverageAlt();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An error has occurred when trying to run \"getGraduatedAverageAltIndicator()\" method from script");
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