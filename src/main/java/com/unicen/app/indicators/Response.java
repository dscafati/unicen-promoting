package com.unicen.app.indicators;

import java.util.DoubleSummaryStatistics;
import java.util.Vector;

public class Response {
    private Integer schoolId;
    private String schoolName;
    private Double value;

    public Response(Integer schoolId, String schoolName, Double value) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.value = value;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public Double getValue() {
        return value;
    }

    public Vector<Object> asVector(){
        Vector<Object> v = new Vector<>();
        v.add(this.getSchoolName());
        v.add(this.getValue());
        return v;
    }

    //para pruebas. Quizas despues no haga falta
    public boolean equals (Object o) {
        Response other = (Response) o;
        return this.getSchoolId().intValue()==other.getSchoolId().intValue();
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
